package edu.course.lab06;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SampleAnalyticsTest {

    private static DataSample sample(String id, String source, int featureCount, SampleStatus status) {
        return new DataSample(id, "label", source, featureCount, status);
    }

    private static List<DataSample> data() {
        List<DataSample> samples = new ArrayList<>();
        samples.add(sample("S-3", "camera-1", 64, SampleStatus.READY));
        samples.add(sample("S-1", "camera-2", 128, SampleStatus.NEW));
        samples.add(sample("S-2", "camera-1", 64, SampleStatus.READY));
        return samples;
    }

    @Test
    void countsSamplesByStatus() {
        Map<SampleStatus, Integer> counts = SampleAnalytics.countByStatus(data());
        assertEquals(Map.of(SampleStatus.READY, 2, SampleStatus.NEW, 1), counts);
    }

    @Test
    void countByStatusOfEmptyListIsEmpty() {
        assertTrue(SampleAnalytics.countByStatus(new ArrayList<>()).isEmpty());
    }

    @Test
    void uniqueSourcesRemoveDuplicatesAndKeepFirstSeenOrder() {
        Set<String> sources = SampleAnalytics.uniqueSources(data());
        assertEquals(List.of("camera-1", "camera-2"), new ArrayList<>(sources));
    }

    @Test
    void uniqueSourcesOfEmptyListIsEmpty() {
        assertTrue(SampleAnalytics.uniqueSources(new ArrayList<>()).isEmpty());
    }

    @Test
    void sortsByFeatureCountThenById() {
        List<DataSample> sorted = SampleAnalytics.sortedByFeatureCountAndId(data());
        assertEquals("S-2", sorted.get(0).getId());
        assertEquals("S-3", sorted.get(1).getId());
        assertEquals("S-1", sorted.get(2).getId());
    }

    @Test
    void sortingDoesNotModifyOriginalList() {
        List<DataSample> original = data();
        SampleAnalytics.sortedByFeatureCountAndId(original);
        assertEquals("S-3", original.get(0).getId());
        assertEquals("S-1", original.get(1).getId());
        assertEquals("S-2", original.get(2).getId());
    }

    @Test
    void sortingReturnsNewList() {
        List<DataSample> original = data();
        List<DataSample> sorted = SampleAnalytics.sortedByFeatureCountAndId(original);
        assertNotSame(original, sorted);
    }

    @Test
    void rejectsNullList() {
        assertThrows(IllegalArgumentException.class, () -> SampleAnalytics.countByStatus(null));
        assertThrows(IllegalArgumentException.class, () -> SampleAnalytics.uniqueSources(null));
        assertThrows(IllegalArgumentException.class,
                () -> SampleAnalytics.sortedByFeatureCountAndId(null));
    }

    @Test
    void rejectsListWithNullElement() {
        List<DataSample> samples = new ArrayList<>();
        samples.add(null);
        assertThrows(IllegalArgumentException.class, () -> SampleAnalytics.countByStatus(samples));
    }
}
