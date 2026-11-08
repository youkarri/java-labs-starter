package edu.course.lab08;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Обрабатывает список входных элементов последовательно или через executor.
 * Класс не хранит состояния, поэтому безопасен для использования из нескольких потоков.
 * Executor принадлежит вызывающему коду, здесь он не завершается.
 */
public class BatchProcessor<T, R> {

    public List<TaskResult<R>> processSequentially(
            List<T> inputs,
            TaskFunction<T, R> function) {
        requireArguments(inputs, function);
        List<TaskResult<R>> results = new ArrayList<>(inputs.size());
        for (int i = 0; i < inputs.size(); i++) {
            results.add(runOne(i, inputs.get(i), function));
        }
        return results;
    }

    public List<TaskResult<R>> processConcurrently(
            List<T> inputs,
            TaskFunction<T, R> function,
            ExecutorService executor) throws InterruptedException {
        requireArguments(inputs, function);
        if (executor == null) {
            throw new IllegalArgumentException("executor must not be null");
        }

        List<Future<TaskResult<R>>> futures = new ArrayList<>(inputs.size());
        try {
            for (int i = 0; i < inputs.size(); i++) {
                futures.add(executor.submit(createTask(i, inputs.get(i), function)));
            }
        } catch (RuntimeException e) {
            cancelAll(futures);
            throw e;
        }

        List<TaskResult<R>> results = new ArrayList<>(inputs.size());
        try {
            for (int i = 0; i < futures.size(); i++) {
                results.add(collect(i, futures.get(i)));
            }
        } catch (InterruptedException e) {
            cancelAll(futures);
            throw e;
        }
        return results;
    }

    private Callable<TaskResult<R>> createTask(int index, T input, TaskFunction<T, R> function) {
        return () -> runOne(index, input, function);
    }

    private TaskResult<R> runOne(int index, T input, TaskFunction<T, R> function) {
        try {
            return TaskResult.success(index, function.apply(input));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return TaskResult.failure(index, e);
        } catch (Exception e) {
            return TaskResult.failure(index, e);
        }
    }

    private TaskResult<R> collect(int index, Future<TaskResult<R>> future) throws InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            return TaskResult.failure(index, new IllegalStateException("task failed unexpectedly", e.getCause()));
        }
    }

    private void cancelAll(List<Future<TaskResult<R>>> futures) {
        for (Future<TaskResult<R>> future : futures) {
            future.cancel(true);
        }
    }

    private void requireArguments(List<T> inputs, TaskFunction<T, R> function) {
        if (inputs == null) {
            throw new IllegalArgumentException("inputs must not be null");
        }
        if (function == null) {
            throw new IllegalArgumentException("function must not be null");
        }
    }
}