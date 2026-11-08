package edu.course.lab07;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringProcessingTest {

    @Test
    void returnsNonEmptyUpperCaseSortedByLengthThenAlphabet() {
        List<String> source = new ArrayList<>(Arrays.asList("java", "", "git", "maven", "api"));
        List<String> result = StringProcessing.nonEmptyUpperSorted(source);
        assertEquals(List.of("API", "GIT", "JAVA", "MAVEN"), result);
    }

    @Test
    void doesNotModifySourceList() {
        List<String> source = new ArrayList<>(Arrays.asList("java", "", "git", "maven", "api"));
        List<String> copy = new ArrayList<>(source);
        StringProcessing.nonEmptyUpperSorted(source);
        assertEquals(copy, source);
    }

    @Test
    void emptyListGivesEmptyResult() {
        assertTrue(StringProcessing.nonEmptyUpperSorted(new ArrayList<>()).isEmpty());
    }

    @Test
    void onlyEmptyStringsGiveEmptyResult() {
        assertTrue(StringProcessing.nonEmptyUpperSorted(List.of("", "")).isEmpty());
    }

    @Test
    void rejectsNullList() {
        assertThrows(IllegalArgumentException.class, () -> StringProcessing.nonEmptyUpperSorted(null));
    }
}
