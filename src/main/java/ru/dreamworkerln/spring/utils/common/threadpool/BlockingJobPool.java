package ru.dreamworkerln.spring.utils.common.threadpool;

import lombok.extern.slf4j.Slf4j;
import net.tascalate.concurrent.CompletableTask;
import net.tascalate.concurrent.Promise;
import net.tascalate.concurrent.Promises;
import net.tascalate.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * Base job pool class
 * <br>
 * Will block calling thread on adding job if queue was overflowed
 * <br>
 * (RejectedExecutionHandler -> CallerRunsPolicy)
 */
@Slf4j
public class BlockingJobPool<A, R> {

    private final ThreadPoolExecutor threadPool;

    private final Duration timeout;

    // On job complete handler
    private final Consumer<JobResult<A, R>> callback;

    // default thread pool name
    private final static String POOL_NAME = "BlockingJobPool";

    /**
     * Constructor
     * @param poolSize thread pool size
     * @param timeout default job execution timeout
     * @param callback default callback for each job
     */
    public BlockingJobPool(Integer poolSize,
                           Duration timeout,
                           Consumer<JobResult<A, R>> callback) {

        this(poolSize, timeout, callback, POOL_NAME);
    }


    /**
     * Constructor
     * @param poolSize thread pool size
     * @param timeout default job execution timeout
     * @param callback default callback for each job
     * @param poolName pool name
     */
    public BlockingJobPool(Integer poolSize,
                           Duration timeout,
                           Consumer<JobResult<A, R>> callback,
                           String poolName) {

        this.timeout = timeout;

        if (poolSize == null || poolSize <= 0) {
            poolSize = 1;
        }

        // Assign empty callback if not specified
        if (callback == null) {
            callback = empty -> {};
        }

        this.callback = callback;

        final CustomizableThreadFactory threadFactory = new CustomizableThreadFactory();
        threadFactory.setDaemon(true);
        threadFactory.setThreadNamePrefix(poolName + "-");



        threadPool = new ThreadPoolTaskExecutor(
            poolSize,poolSize,
            60,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            //new LinkedBlockingQueue<>(poolSize),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());

    }

    /**
     * Asynchronously start job, on job complete will call default BlockingJobPool.callback
     * @param argument identifier to identify task
     * @param function method to execute asynchronously (job)

     */
    public void add(A argument, Function<A, JobResult<A,R>> function) {
        add(argument, function, callback);
    }


    /**
     * Asynchronously start job, on job complete will call custom callback
     * @param argument identifier to identify task
     * @param function method to execute asynchronously (job)
     * @param callback custom consumer to be executed after job finished
     */
    public void add(A argument, Function<A, JobResult<A,R>> function, Consumer<JobResult<A,R>> callbackCustom) {
        CompletableTask
            .supplyAsync(() -> function.apply(argument), threadPool)
            // выставляем timeout, начинает отсчитываться при вызове supplyAsync(),
            // даже если задача попала в ThreadPoolExecutor.workQueue
            .orTimeout(timeout)
            // generate JobResult on exceptional execution
            .exceptionallyAsync(throwable -> {
                    JobResult<A,R> r = new JobResult<>(argument);
                    r.setException(throwable);
                    return r;
                }
            )
            // Оповещаем о завершении каждой задачи
            .thenAcceptAsync(
                callbackCustom
            )
            .exceptionallyAsync(throwable -> {
                    log.error("blockingJobPool.callback have been executed with error: ", throwable);
                    return null;
                }
            );
    }


    /**
     * Synchronously complete job with default threadPoolExecutor timeout
     * @param argument identifier to identify task
     * @param function method to execute synchronously
     * @return JobResult<A,R> result with identifier or execution exception (if had any)
     */
    public JobResult<A,R> execTimeout(A argument, Function<A, JobResult<A,R>> function) throws ExecutionException, InterruptedException {

        return execTimeout(argument, function, timeout);
    }


    /**
     * Synchronously complete job with customizable timeout
     * @param argument - identifier to identify task
     * @param function - method to execute synchronously
     * @param timeout - specified timeout
     * @return JobResult<A,R> result with identifier or execution exception (if had any)
     */
    public JobResult<A,R> execTimeout(A argument, Function<A, JobResult<A,R>> function, Duration timeout) throws ExecutionException, InterruptedException {

        return
            CompletableTask
                .supplyAsync(() -> function.apply(argument), threadPool)
                .orTimeout(timeout)
                .handle((t, throwable) -> {
                    if (throwable != null) {
                        t = new JobResult<>(argument);
                        t.setException(throwable);
                    }
                    return t;
                })
                .get();
    }



    /**
     * Start blocking batch jobs.
     * <br>
     * Will block calling thread till all promises have been completed
     @return List<JobResult>
     */
    public List<JobResult<A,R>> batchBlocking(List<BatchItem<A, R>> batchList) throws ExecutionException, InterruptedException {

        List<Promise<JobResult<A,R>>> promiseList = batchJobs(batchList);
        return Promises.all(promiseList).get();
    }


    /**
     * Start batch jobs asynchronously, no wait, no results
     */
    public void batchAsync(List<BatchItem<A, R>> batchList) {
        batchJobs(batchList);
    }


    /**
     * Start batch jobs asynchronously, call callback when all jobs have been completed
     * @param batchList jobs to do
     * @param callback call when all jobs done
     */
    public void batchAsync(List<BatchItem<A, R>> batchList, Consumer<List<JobResult<A,R>>> callback) {

        List<Promise<JobResult<A,R>>> promiseList = batchJobs(batchList);
        Promises.all(promiseList).thenAccept(callback);
    }

    // --------------------------------------------------------------------

    public boolean shutdown() throws InterruptedException {
        System.out.println("Awaiting termination ...");
        threadPool.shutdown();
        return threadPool.awaitTermination(10000, TimeUnit.MILLISECONDS);
    }
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return threadPool.awaitTermination(timeout, unit);
    }

    public boolean isTerminated()  {
        return  threadPool.isTerminated();
    }


    // ========================================================================


    private List<Promise<JobResult<A,R>>> batchJobs(List<BatchItem<A, R>> batchList) {

        List<Promise<JobResult<A,R>>> result = new ArrayList<>();

        batchList.forEach(item -> {

            Promise<JobResult<A,R>> promise =
                CompletableTask
                    .supplyAsync(() -> item.getFunction().apply(item.getArgument()), threadPool)
                    .orTimeout(timeout)
                    .handle((t, throwable) -> {
                        if (throwable != null) {
                            t = new JobResult<>(item.getArgument());
                            t.setException(throwable);
                        }
                        return t;
                    });
            result.add(promise);
        });

        return result;
    }
}



/*
            .handleAsync((t, throwable) -> {
                if (throwable != null) {
                    t = new JobResult<>(identifier);
                    t.setException(throwable);
                }
                return t;
            })


    public void add(A argument, Function<A, JobResult<A,R>> function) {

        //System.out.println("callback: + " + Thread.currentThread().getName());
        CompletableTask
            .supplyAsync(() -> function.apply(argument), threadPool)
            // выставляем timeout, начинает отсчитываться при вызове supplyAsync(),
            // даже если задача попала в ThreadPoolExecutor.workQueue
            .orTimeout(timeout)
            // generate JobResult on exceptional execution
            .exceptionallyAsync(throwable -> {
                    JobResult<A,R> r = new JobResult<>(argument);
                    r.setException(throwable);
                    return r;
                }
            )
            // Оповещаем о завершении каждой задачи
            .thenAcceptAsync(
                callback
            )
            .exceptionallyAsync(throwable -> {
                    log.error("blockingJobPool.callback have been executed with error: ", throwable);
                    return null;
                }
            );

    }
    */