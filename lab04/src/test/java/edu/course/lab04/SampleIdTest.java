package edu.course.lab04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SampleIdTest {

    @Test
    void idsWithSameFieldsAreEqualAndHaveSameHashCode() {
        SampleId first = new SampleId("train", 5);
        SampleId second = new SampleId("train", 5);
        assertTrue(first.equals(second));
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void idsWithDifferentDatasetAreNotEqual() {
        assertFalse(new SampleId("train", 5).equals(new SampleId("test", 5)));
    }

    @Test
    void idsWithDifferentNumberAreNotEqual() {
        assertFalse(new SampleId("train", 5).equals(new SampleId("train", 6)));
    }

    @Test
    void comparisonWithNullAndOtherTypeIsFalse() {
        SampleId id = new SampleId("train", 5);
        assertFalse(id.equals(null));
        assertFalse(id.equals("train"));
    }

    @Test
    void rejectsBlankDatasetName() {
        assertThrows(IllegalArgumentException.class, () -> new SampleId("", 1));
    }

    @Test
    void rejectsNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> new SampleId("train", -1));
    }

    @Test
    void differentIdsMayShareHashCode() {
        SampleId first = new SampleId("Aa", 1);
        SampleId second = new SampleId("BB", 1);
        assertEquals(first.hashCode(), second.hashCode());
        assertFalse(first.equals(second));
    }
}