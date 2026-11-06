package edu.course.lab04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SampleRegistryTest {

    private static DataSample sample(String id) {
        return new DataSample(id, "cat", SampleStatus.NEW, new double[]{1.0, 2.0});
    }

    @Test
    void putAndGetReturnSameValue() {
        SampleRegistry registry = new SampleRegistry();
        DataSample value = sample("s1");
        registry.put(new SampleId("train", 1), value);
        assertSame(value, registry.get(new SampleId("train", 1)));
    }

    @Test
    void twoEqualKeyInstancesFindSameValue() {
        SampleRegistry registry = new SampleRegistry();
        SampleId first = new SampleId("train", 1);
        SampleId second = new SampleId("train", 1);
        DataSample value = sample("s1");
        registry.put(first, value);
        assertNotSame(first, second);
        assertSame(value, registry.get(second));
    }

    @Test
    void putWithEqualKeyReplacesValueAndKeepsSize() {
        SampleRegistry registry = new SampleRegistry();
        DataSample oldValue = sample("old");
        DataSample newValue = sample("new");
        registry.put(new SampleId("train", 1), oldValue);
        registry.put(new SampleId("train", 1), newValue);
        assertEquals(1, registry.size());
        assertSame(newValue, registry.get(new SampleId("train", 1)));
    }

    @Test
    void getMissingKeyReturnsNull() {
        assertNull(new SampleRegistry().get(new SampleId("train", 1)));
    }

    @Test
    void removeDeletesValue() {
        SampleRegistry registry = new SampleRegistry();
        DataSample value = sample("s1");
        registry.put(new SampleId("train", 1), value);
        assertSame(value, registry.remove(new SampleId("train", 1)));
        assertFalse(registry.containsKey(new SampleId("train", 1)));
        assertEquals(0, registry.size());
    }

    @Test
    void removeMissingKeyReturnsNull() {
        assertNull(new SampleRegistry().remove(new SampleId("train", 1)));
    }

    @Test
    void containsKeyWorksForPresentAndAbsentKeys() {
        SampleRegistry registry = new SampleRegistry();
        registry.put(new SampleId("train", 1), sample("s1"));
        assertTrue(registry.containsKey(new SampleId("train", 1)));
        assertFalse(registry.containsKey(new SampleId("train", 2)));
    }

    @Test
    void rejectsNullArguments() {
        SampleRegistry registry = new SampleRegistry();
        assertThrows(IllegalArgumentException.class,
                () -> registry.put(null, sample("s1")));
        assertThrows(IllegalArgumentException.class,
                () -> registry.put(new SampleId("train", 1), null));
    }

    @Test
    void collidingKeysKeepDifferentValues() {
        SampleRegistry registry = new SampleRegistry();
        SampleId first = new SampleId("Aa", 1);
        SampleId second = new SampleId("BB", 1);
        assertEquals(first.hashCode(), second.hashCode());
        DataSample firstValue = sample("first");
        DataSample secondValue = sample("second");
        registry.put(first, firstValue);
        registry.put(second, secondValue);
        assertEquals(2, registry.size());
        assertSame(firstValue, registry.get(first));
        assertSame(secondValue, registry.get(second));
    }
}