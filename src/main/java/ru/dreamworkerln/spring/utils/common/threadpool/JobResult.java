package ru.dreamworkerln.spring.utils.common.threadpool;

import lombok.Data;


/**
 * Job result
 * @param <A> Argument with whom job was called (identity purposes)
 * @param <R> Result
 */
@Data
public class JobResult<A, R> {

    /**
     *  Argument with whom job was called (identity purposes)
     */
    private A argument;

    /**
     * Job result (null if job exceptionally failed)
     */
    private R result;

    /**
     * Job fail exception (null if was executed successfully)
     */
    private Throwable exception;

    public JobResult() {}

    public JobResult(A argument) {
        this.argument = argument;
    }

    public JobResult(A argument, R result) {
        this.argument = argument;
        this.result = result;
    }

    @Override
    public String toString() {
        String res = result.toString();
        if (exception != null) {
            res += ", exception= " + exception;
        }
        return res;
    }
}
