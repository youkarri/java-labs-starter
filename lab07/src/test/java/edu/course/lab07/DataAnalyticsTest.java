package edu.course.lab07;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DataAnalyticsTest {

    private static DataSample sample(String id, String label, String source, int features, SampleStatus status) {
        return new DataSample(id, label, source, features, status);
    }

    private static List<DataSample> data() {
        List<DataSample> samples = new ArrayList<>();
        samples.add(sample("S-3", " Cat ", "camera-1", 64, SampleStatus.READY));
        samples.add(sample("S-1", "dog", "camera-2", 128, SampleStatus.NEW));
        samples.add(sample("S-2", "BIRD", "camera-1", 64, SampleStatus.READY));
        samples.add(sample("S-4", "cat", "sensor-1", 32, SampleStatus.REJECTED));
        return samples;
    }

    private static List<String> ids(List<DataSample> samples) {
        return samples.stream().map(DataSample::getId).toList();
    }

    // filterByStatus

    @Test
    void filterOfEmptyListIsEmpty() {
        assertTrue(DataAnalytics.filterByStatus(new ArrayList<>(), SampleStatus.READY).isEmpty());
    }

    @Test
    void filterOfSingleMatchingElement() {
        List<DataSample> single = List.of(sample("S-1", "cat", "camera-1", 10, SampleStatus.READY));
        assertEquals(List.of("S-1"), ids(DataAnalytics.filterByStatus(single, SampleStatus.READY)));
    }

    @Test
    void filterKeepsOnlyRequestedStatusInOriginalOrder() {
        List<DataSample> result = DataAnalytics.filterByStatus(data(), SampleStatus.READY);
        assertEquals(List.of("S-3", "S-2"), ids(result));
    }

    @Test
    void filterWithoutMatchesIsEmpty() {
        assertTrue(DataAnalytics.filterByStatus(data(), SampleStatus.LABELED).isEmpty());
    }

    // normalizedLabels

    @Test
    void normalizedLabelsAreTrimmedLowerCaseAndSorted() {
        assertEquals(List.of("bird", "cat", "cat", "dog"), DataAnalytics.normalizedLabels(data()));
    }

    @Test
    void normalizedLabelsOfEmptyListIsEmpty() {
        assertTrue(DataAnalytics.normalizedLabels(new ArrayList<>()).isEmpty());
    }

    // countBySource

    @Test
    void countsBySourceForSeveralGroups() {
        Map<String, Long> counts = DataAnalytics.countBySource(data());
        assertEquals(Map.of("camera-1", 2L, "camera-2", 1L, "sensor-1", 1L), counts);
    }

    @Test
    void countBySourceKeysAreInAlphabeticalOrder() {
        List<String> keys = new ArrayList<>(DataAnalytics.countBySource(data()).keySet());
        assertEquals(List.of("camera-1", "camera-2", "sensor-1"), keys);
    }

    @Test
    void countBySourceOfEmptyListIsEmpty() {
        assertTrue(DataAnalytics.countBySource(new ArrayList<>()).isEmpty());
    }

    @Test
    void countBySourceOfSingleElement() {
        List<DataSample> single = List.of(sample("S-1", "cat", "camera-1", 10, SampleStatus.NEW));
        assertEquals(Map.of("camera-1", 1L), DataAnalytics.countBySource(single));
    }

    // maxByFeatureCount

    @Test
    void maxOfEmptyListIsEmptyOptional() {
        assertEquals(Optional.empty(), DataAnalytics.maxByFeatureCount(new ArrayList<>()));
    }

    @Test
    void maxFindsSampleWithMostFeatures() {
        Optional<DataSample> max = DataAnalytics.maxByFeatureCount(data());
        assertTrue(max.isPresent());
        assertEquals("S-1", max.get().getId());
    }

    @Test
    void maxOfSingleElementIsThatElement() {
        List<DataSample> single = List.of(sample("S-9", "cat", "camera-1", 10, SampleStatus.NEW));
        assertEquals("S-9", DataAnalytics.maxByFeatureCount(single).map(DataSample::getId).orElse("none"));
    }

    @Test
    void maxWithTieReturnsFirstInListOrder() {
        List<DataSample> tie = List.of(
                sample("S-1", "a", "x", 50, SampleStatus.NEW),
                sample("S-2", "b", "x", 50, SampleStatus.NEW));
        assertEquals("S-1", DataAnalytics.maxByFeatureCount(tie).map(DataSample::getId).orElse("none"));
    }

    // sortedByFeatureCountAndId

    @Test
    void sortsByFeatureCountThenById() {
        List<DataSample> sorted = DataAnalytics.sortedByFeatureCountAndId(data());
        assertEquals(List.of("S-4", "S-2", "S-3", "S-1"), ids(sorted));
    }

    @Test
    void sortedOfEmptyListIsEmpty() {
        assertTrue(DataAnalytics.sortedByFeatureCountAndId(new ArrayList<>()).isEmpty());
    }

    @Test
    void sortingDoesNotModifyInputList() {
        List<DataSample> input = data();
        List<DataSample> copy = new ArrayList<>(input);
        DataAnalytics.sortedByFeatureCountAndId(input);
        assertEquals(copy, input);
    }

    @Test
    void sortingReturnsNewList() {
        List<DataSample> input = data();
        assertNotSame(input, DataAnalytics.sortedByFeatureCountAndId(input));
    }

    // averageFeatureCountBySource

    @Test
    void averageFeatureCountBySource() {
        Map<String, Double> averages = DataAnalytics.averageFeatureCountBySource(data());
        assertEquals(Map.of("camera-1", 64.0, "camera-2", 128.0, "sensor-1", 32.0), averages);
    }

    @Test
    void averageOfEmptyListIsEmpty() {
        assertTrue(DataAnalytics.averageFeatureCountBySource(new ArrayList<>()).isEmpty());
    }

    // входные данные

    @Test
    void otherMethodsDoNotModifyInputList() {
        List<DataSample> input = data();
        List<DataSample> copy = new ArrayList<>(input);
        DataAnalytics.filterByStatus(input, SampleStatus.READY);
        DataAnalytics.normalizedLabels(input);
        DataAnalytics.countBySource(input);
        DataAnalytics.maxByFeatureCount(input);
        DataAnalytics.averageFeatureCountBySource(input);
        assertEquals(copy, input);
    }

    @Test
    void rejectsNullList() {
        assertThrows(IllegalArgumentException.class,
                () -> DataAnalytics.filterByStatus(null, SampleStatus.NEW));
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.normalizedLabels(null));
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.countBySource(null));
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.maxByFeatureCount(null));
        assertThrows(IllegalArgumentException.class,
                () -> DataAnalytics.sortedByFeatureCountAndId(null));
        assertThrows(IllegalArgumentException.class,
                () -> DataAnalytics.averageFeatureCountBySource(null));
    }

    @Test
    void rejectsNullStatusAndNullElement() {
        assertThrows(IllegalArgumentException.class,
                () -> DataAnalytics.filterByStatus(data(), null));
        List<DataSample> withNull = new ArrayList<>();
        withNull.add(null);
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.countBySource(withNull));
    }
}
