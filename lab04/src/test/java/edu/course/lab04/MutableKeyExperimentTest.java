package edu.course.lab04;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MutableKeyExperimentTest {

    private static class MutableKey {
        private int number;

        private MutableKey(int number) {
            this.number = number;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            return number == ((MutableKey) other).number;
        }

        @Override
        public int hashCode() {
            return Objects.hash(number);
        }
    }

    @Test
    void valueIsFoundBeforeKeyChanges() {
        Map<MutableKey, String> map = new HashMap<>();
        map.put(new MutableKey(1), "value");
        assertEquals("value", map.get(new MutableKey(1)));
    }

    @Test
    void valueIsLostAfterKeyFieldChanges() {
        Map<MutableKey, String> map = new HashMap<>();
        MutableKey key = new MutableKey(1);
        map.put(key, "value");

        key.number = 2;

        assertNull(map.get(key));
        assertNull(map.get(new MutableKey(1)));
        assertFalse(map.containsKey(key));
        assertEquals(1, map.size());
    }
}