package edu.course.lab07;

import java.util.Comparator;
import java.util.List;

/**
 * Подготовительная часть: обработка списка строк через Stream API.
 */
public final class StringProcessing {

    private static final Comparator<String> BY_LENGTH_THEN_ALPHABET =
            Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder());

    private StringProcessing() {
    }

    /**
     * Возвращает новый список непустых строк в верхнем регистре,
     * отсортированный сначала по длине, затем по алфавиту.
     * Исходный список не изменяется.
     */
    public static List<String> nonEmptyUpperSorted(List<String> source) {
        if (source == null) {
            throw new IllegalArgumentException("source must not be null");
        }
        return source.stream()
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .sorted(BY_LENGTH_THEN_ALPHABET)
                .toList();
    }
}
