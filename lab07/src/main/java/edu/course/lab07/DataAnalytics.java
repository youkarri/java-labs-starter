package edu.course.lab07;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Аналитика над списком наблюдений через Stream API.
 * Методы ничего не печатают и не изменяют входной список.
 */
public final class DataAnalytics {

    private static final Comparator<DataSample> BY_FEATURE_COUNT_THEN_ID =
            Comparator.comparingInt(DataSample::getFeatureCount)
                    .thenComparing(DataSample::getId);

    private DataAnalytics() {
    }

    /** Наблюдения с указанным статусом в исходном порядке. */
    public static List<DataSample> filterByStatus(List<DataSample> samples, SampleStatus status) {
        requireValid(samples);
        if (status == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        return samples.stream()
                .filter(sample -> sample.getStatus() == status)
                .toList();
    }

    /** Метки без пробелов по краям, в нижнем регистре, отсортированные по алфавиту. */
    public static List<String> normalizedLabels(List<DataSample> samples) {
        requireValid(samples);
        return samples.stream()
                .map(DataSample::getLabel)
                .map(label -> label.trim().toLowerCase(Locale.ROOT))
                .sorted()
                .toList();
    }

    /** Число наблюдений по источникам. Ключи упорядочены по алфавиту. */
    public static Map<String, Long> countBySource(List<DataSample> samples) {
        requireValid(samples);
        return samples.stream()
                .collect(Collectors.groupingBy(
                        DataSample::getSource,
                        TreeMap::new,
                        Collectors.counting()));
    }

    /** Наблюдение с максимальным числом признаков. При равенстве берется первое по порядку списка. */
    public static Optional<DataSample> maxByFeatureCount(List<DataSample> samples) {
        requireValid(samples);
        return samples.stream()
                .max(Comparator.comparingInt(DataSample::getFeatureCount));
    }

    /** Новый список, отсортированный по числу признаков, затем по id. */
    public static List<DataSample> sortedByFeatureCountAndId(List<DataSample> samples) {
        requireValid(samples);
        return samples.stream()
                .sorted(BY_FEATURE_COUNT_THEN_ID)
                .toList();
    }

    /** Дополнительно. Среднее число признаков по источникам. Ключи упорядочены по алфавиту. */
    public static Map<String, Double> averageFeatureCountBySource(List<DataSample> samples) {
        requireValid(samples);
        return samples.stream()
                .collect(Collectors.groupingBy(
                        DataSample::getSource,
                        TreeMap::new,
                        Collectors.averagingInt(DataSample::getFeatureCount)));
    }

    private static void requireValid(List<DataSample> samples) {
        if (samples == null) {
            throw new IllegalArgumentException("samples must not be null");
        }
        if (samples.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("samples must not contain null");
        }
    }
}
