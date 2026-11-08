package edu.course.lab04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChainedHashTableTest {

    private static DataSample sample(String id) {
        return new DataSample(id, "cat", SampleStatus.NEW, new double[]{1.0});
    }

    @Test
    void putAndGetByEqualKey() {
        ChainedHashTable table = new ChainedHashTable(8);
        DataSample value = sample("s1");
        table.put(new SampleId("train", 1), value);
        assertSame(value, table.get(new SampleId("train", 1)));
        assertEquals(1, table.size());
    }

    @Test
    void putWithEqualKeyReplacesValue() {
        ChainedHashTable table = new ChainedHashTable(8);
        DataSample newValue = sample("new");
        table.put(new SampleId("train", 1), sample("old"));
        table.put(new SampleId("train", 1), newValue);
        assertEquals(1, table.size());
        assertSame(newValue, table.get(new SampleId("train", 1)));
    }

    @Test
    void collidingKeysShareBucketButKeepDifferentValues() {
        ChainedHashTable table = new ChainedHashTable(8);
        SampleId first = new SampleId("Aa", 1);
        SampleId second = new SampleId("BB", 1);
        DataSample firstValue = sample("first");
        DataSample secondValue = sample("second");
        table.put(first, firstValue);
        table.put(second, secondValue);
        assertEquals(2, table.size());
        assertSame(firstValue, table.get(first));
        assertSame(secondValue, table.get(second));
    }

    @Test
    void removeOneOfCollidingKeysKeepsOther() {
        ChainedHashTable table = new ChainedHashTable(8);
        SampleId first = new SampleId("Aa", 1);
        SampleId second = new SampleId("BB", 1);
        DataSample secondValue = sample("second");
        table.put(first, sample("first"));
        table.put(second, secondValue);
        assertNotNull(table.remove(first));
        assertFalse(table.containsKey(first));
        assertSame(secondValue, table.get(second));
        assertEquals(1, table.size());
    }

    @Test
    void removeMissingKeyReturnsNull() {
        ChainedHashTable table = new ChainedHashTable(8);
        assertNull(table.remove(new SampleId("train", 1)));
        assertEquals(0, table.size());
    }

    @Test
    void worksWithSingleBucket() {
        ChainedHashTable table = new ChainedHashTable(1);
        for (int i = 0; i < 5; i++) {
            table.put(new SampleId("train", i), sample("s" + i));
        }
        assertEquals(5, table.size());
        assertTrue(table.containsKey(new SampleId("train", 3)));
    }

    @Test
    void rejectsNonPositiveBucketCount() {
        assertThrows(IllegalArgumentException.class, () -> new ChainedHashTable(0));
    }
}