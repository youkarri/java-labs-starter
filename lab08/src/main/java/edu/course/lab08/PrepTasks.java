package edu.course.lab08;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Подготовительная часть: три задачи Callable через фиксированный executor.
 */
public final class PrepTasks {

    private PrepTasks() {
    }

    /**
     * Запускает три задачи (10, 20, 30) в пуле из двух потоков
     * и возвращает результаты в порядке отправки задач.
     */
    public static List<Integer> runThreeTasks() throws InterruptedException, ExecutionException {
        List<Callable<Integer>> tasks = List.of(() -> 10, () -> 20, () -> 30);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Future<Integer>> futures = new ArrayList<>();
            for (Callable<Integer> task : tasks) {
                futures.add(executor.submit(task));
            }
            List<Integer> results = new ArrayList<>();
            for (Future<Integer> future : futures) {
                results.add(future.get());
            }
            return results;
        } finally {
            executor.shutdown();
        }
    }
}
