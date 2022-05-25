package ru.dreamworkerln.spring.utils.common.threadpool;

import lombok.Data;
import lombok.Getter;

import java.util.function.Function;

/**
 * Batch job item
 * @param <A> job argument (identifier)
 * @param <R> job result
 */
@Getter
public class BatchItem<A, R> {

    /**
     * Job argument
     */
    public final A argument;

    /**
     * Function (a, res(a,r)) -> function that do the job with arg a and return result(a,r)
     */
    public final Function<A, JobResult<A,R>> function;

    public BatchItem(A argument, Function<A, JobResult<A, R>> function) {
        this.argument = argument;
        this.function = function;
    }
}
