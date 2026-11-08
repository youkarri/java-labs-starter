package edu.course.lab06;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilsTest {

    @Test
    void copiesIntegersIntoListOfNumbers() {
        List<Integer> source = List.of(1, 2, 3);
        List<Number> target = new ArrayList<>();
        CollectionUtils.copyAll(source, target);
        assertEquals(3, target.size());
        assertEquals(Integer.valueOf(2), target.get(1));
    }

    @Test
    void copiesStringsIntoListOfObjects() {
        List<String> source = List.of("a", "b");
        List<Object> target = new ArrayList<>();
        CollectionUtils.copyAll(source, target);
        assertEquals(List.of("a", "b"), target);
    }

    @Test
    void keepsExistingElementsOfTarget() {
        List<Object> target = new ArrayList<>();
        target.add("first");
        CollectionUtils.copyAll(List.of("second"), target);
        assertEquals(List.of("first", "second"), target);
    }

    @Test
    void emptySourceLeavesTargetUnchanged() {
        List<Object> target = new ArrayList<>();
        target.add("x");
        CollectionUtils.copyAll(new ArrayList<String>(), target);
        assertEquals(1, target.size());
    }

    @Test
    void rejectsNullArguments() {
        List<Object> target = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> CollectionUtils.copyAll(null, target));
        assertThrows(IllegalArgumentException.class,
                () -> CollectionUtils.copyAll(List.of("a"), null));
    }
}
