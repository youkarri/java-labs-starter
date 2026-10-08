package edu.course.lab08;

import java.util.List;
import java.util.concurrent.ExecutorService;

/** Учебный каркас: оба метода реализует студент в ЛР 8. */
public final class BatchProcessor<T, R> {

    public List<TaskResult<R>> processSequentially(
            List<T> inputs,
            TaskFunction<T, R> function) {
        throw new UnsupportedOperationException("Implement in lab08");
    }

    public List<TaskResult<R>> processConcurrently(
            List<T> inputs,
            TaskFunction<T, R> function,
            ExecutorService executor) throws InterruptedException {
        throw new UnsupportedOperationException("Implement in lab08");
    }
}
