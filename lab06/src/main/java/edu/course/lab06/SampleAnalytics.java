package edu.course.lab06;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Аналитика по списку наблюдений. Исходный список никогда не изменяется.
 */
public final class SampleAnalytics {

    /** Порядок по числу признаков, при равенстве по идентификатору. */
    private static final class ByFeatureCountThenId implements Comparator<DataSample> {
        @Override
        public int compare(DataSample first, DataSample second) {
            int byCount = Integer.compare(first.getFeatureCount(), second.getFeatureCount());
            if (byCount != 0) {
                return byCount;
            }
            return first.getId().compareTo(second.getId());
        }
    }

    private SampleAnalytics() {
        // Утилитарный класс не должен иметь экземпляров.
    }

    /** Число наблюдений по статусам. В карте только те статусы, которые встретились. */
    public static Map<SampleStatus, Integer> countByStatus(List<DataSample> samples) {
        requireValid(samples);
        Map<SampleStatus, Integer> counts = new EnumMap<>(SampleStatus.class);
        for (DataSample sample : samples) {
            SampleStatus status = sample.getStatus();
            counts.put(status, counts.getOrDefault(status, 0) + 1);
        }
        return counts;
    }

    /** Уникальные источники в порядке первого появления. */
    public static Set<String> uniqueSources(List<DataSample> samples) {
        requireValid(samples);
        Set<String> sources = new LinkedHashSet<>();
        for (DataSample sample : samples) {
            sources.add(sample.getSource());
        }
        return sources;
    }

    /** Новый список, отсортированный по числу признаков и идентификатору. */
    public static List<DataSample> sortedByFeatureCountAndId(List<DataSample> samples) {
        requireValid(samples);
        List<DataSample> sorted = new ArrayList<>(samples);
        sorted.sort(new ByFeatureCountThenId());
        return sorted;
    }

    private static void requireValid(List<DataSample> samples) {
        if (samples == null) {
            throw new IllegalArgumentException("samples must not be null");
        }
        for (DataSample sample : samples) {
            if (sample == null) {
                throw new IllegalArgumentException("samples must not contain null");
            }
        }
    }
}
