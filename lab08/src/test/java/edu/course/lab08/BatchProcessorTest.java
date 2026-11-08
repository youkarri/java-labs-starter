package edu.course.lab08;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BatchProcessorTest {

    private final BatchProcessor<Integer, Integer> processor = new BatchProcessor<>();

    private static final TaskFunction<Integer, Integer> TIMES_TEN = x -> x * 10;

    private static final TaskFunction<Integer, Integer> FAILS_ON_TWO = x -> {
        if (x == 2) {
            throw new IllegalArgumentException("bad 2");
        }
        return x * 10;
    };

    // последовательная обработка

    @Test
    void sequentialEmptyInputGivesEmptyResult() {
        assertTrue(processor.processSequentially(new ArrayList<>(), TIMES_TEN).isEmpty());
    }

    @Test
    void sequentialProcessesAllInOrder() {
        List<TaskResult<Integer>> results = processor.processSequentially(List.of(1, 2, 3), TIMES_TEN);
        assertEquals(3, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertEquals(i, results.get(i).index());
            assertTrue(results.get(i).isSuccess());
        }
        assertEquals(Integer.valueOf(10), results.get(0).value());
        assertEquals(Integer.valueOf(20), results.get(1).value());
        assertEquals(Integer.valueOf(30), results.get(2).value());
    }

    @Test
    void sequentialOneFailureKeepsOtherResults() {
        List<TaskResult<Integer>> results = processor.processSequentially(List.of(1, 2, 3), FAILS_ON_TWO);
        assertTrue(results.get(0).isSuccess());
        assertFalse(results.get(1).isSuccess());
        assertEquals("bad 2", results.get(1).error().getMessage());
        assertTrue(results.get(2).isSuccess());
        assertEquals(Integer.valueOf(30), results.get(2).value());
    }

    @Test
    void sequentialDoesNotModifyInput() {
        List<Integer> inputs = new ArrayList<>(List.of(3, 1, 2));
        List<Integer> copy = new ArrayList<>(inputs);
        processor.processSequentially(inputs, FAILS_ON_TWO);
        assertEquals(copy, inputs);
    }

    @Test
    void sequentialRejectsNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> processor.processSequentially(null, TIMES_TEN));
        assertThrows(IllegalArgumentException.class, () -> processor.processSequentially(List.of(1), null));
    }

    // конкурентная обработка

    @Test
    void concurrentEmptyInputGivesEmptyResult() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            assertTrue(processor.processConcurrently(new ArrayList<>(), TIMES_TEN, executor).isEmpty());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void concurrentProcessesAllInOrder() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<TaskResult<Integer>> results =
                    processor.processConcurrently(List.of(1, 2, 3, 4, 5), TIMES_TEN, executor);
            assertEquals(5, results.size());
            for (int i = 0; i < results.size(); i++) {
                assertEquals(i, results.get(i).index());
                assertEquals(Integer.valueOf((i + 1) * 10), results.get(i).value());
            }
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void concurrentKeepsInputOrderWhenTasksFinishInDifferentOrder() throws Exception {
        CountDownLatch secondFinished = new CountDownLatch(1);
        TaskFunction<Integer, String> function = x -> {
            if (x == 1) {
                // первая задача ждёт завершения второй, поэтому заканчивается позже неё
                if (!secondFinished.await(5, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("second task did not finish");
                }
            } else {
                secondFinished.countDown();
            }
            return "value-" + x;
        };
        BatchProcessor<Integer, String> stringProcessor = new BatchProcessor<>();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<TaskResult<String>> results =
                    stringProcessor.processConcurrently(List.of(1, 2), function, executor);
            assertEquals("value-1", results.get(0).value());
            assertEquals("value-2", results.get(1).value());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void concurrentOneFailureKeepsOtherResults() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<TaskResult<Integer>> results =
                    processor.processConcurrently(List.of(1, 2, 3), FAILS_ON_TWO, executor);
            assertTrue(results.get(0).isSuccess());
            assertFalse(results.get(1).isSuccess());
            assertEquals("bad 2", results.get(1).error().getMessage());
            assertTrue(results.get(2).isSuccess());
            assertEquals(Integer.valueOf(30), results.get(2).value());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void concurrentDoesNotModifyInput() throws Exception {
        List<Integer> inputs = new ArrayList<>(List.of(3, 1, 2));
        List<Integer> copy = new ArrayList<>(inputs);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            processor.processConcurrently(inputs, FAILS_ON_TWO, executor);
        } finally {
            executor.shutdown();
        }
        assertEquals(copy, inputs);
    }

    @Test
    void processorDoesNotShutDownExecutorAndOwnerDoes() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            processor.processConcurrently(List.of(1, 2), TIMES_TEN, executor);
            assertFalse(executor.isShutdown());
        } finally {
            executor.shutdown();
        }
        assertTrue(executor.isShutdown());
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }

    @Test
    void concurrentRejectsNullArguments() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            assertThrows(IllegalArgumentException.class,
                    () -> processor.processConcurrently(null, TIMES_TEN, executor));
            assertThrows(IllegalArgumentException.class,
                    () -> processor.processConcurrently(List.of(1), null, executor));
            assertThrows(IllegalArgumentException.class,
                    () -> processor.processConcurrently(List.of(1), TIMES_TEN, null));
        } finally {
            executor.shutdown();
        }
    }

    // работа с наборами данных (вариант ИИ)

    @Test
    void processesDatasetPartsAndReportsInvalidPart() throws Exception {
        BatchProcessor<double[], Stats> statsProcessor = new BatchProcessor<>();
        List<double[]> parts = List.of(
                new double[] {1.0, 2.0, 3.0},
                new double[0],
                new double[] {10.0, 20.0});
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<TaskResult<Stats>> results =
                    statsProcessor.processConcurrently(parts, new StatsFunction(), executor);
            assertEquals(6.0, results.get(0).value().sum(), 1e-9);
            assertEquals(2.0, results.get(0).value().average(), 1e-9);
            assertFalse(results.get(1).isSuccess());
            assertEquals(30.0, results.get(2).value().sum(), 1e-9);
            assertEquals(15.0, results.get(2).value().average(), 1e-9);
        } finally {
            executor.shutdown();
        }
    }

    // дополнительная часть: прерывание

    @Test
    void interruptionIsPropagatedAndRunningTaskIsCancelled() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch taskInterrupted = new CountDownLatch(1);
        TaskFunction<Integer, String> blocking = x -> {
            started.countDown();
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException e) {
                taskInterrupted.countDown();
                throw e;
            }
            return "never";
        };
        BatchProcessor<Integer, String> stringProcessor = new BatchProcessor<>();
        Thread caller = Thread.currentThread();
        Thread interrupter = new Thread(() -> {
            try {
                started.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                return;
            }
            caller.interrupt();
        });
        ExecutorService executor = Executors.newFixedThreadPool(1);
        try {
            interrupter.start();
            assertThrows(InterruptedException.class,
                    () -> stringProcessor.processConcurrently(List.of(1, 2), blocking, executor));
            assertTrue(taskInterrupted.await(5, TimeUnit.SECONDS));
            assertFalse(executor.isShutdown());
        } finally {
            executor.shutdown();
        }
    }
}
