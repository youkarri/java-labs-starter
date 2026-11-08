package edu.course.lab05;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldSplitterTest {

    @Test
    void splitsPlainFields() {
        assertArrayEquals(new String[]{"a", "b", "c"}, FieldSplitter.split("a;b;c"));
    }

    @Test
    void keepsEmptyFields() {
        assertArrayEquals(new String[]{"a", "", "c", ""}, FieldSplitter.split("a;;c;"));
    }

    @Test
    void emptyLineGivesOneEmptyField() {
        assertArrayEquals(new String[]{""}, FieldSplitter.split(""));
    }

    @Test
    void quotedFieldMayContainDelimiter() {
        assertArrayEquals(new String[]{"a", "b;c", "d"}, FieldSplitter.split("a;\"b;c\";d"));
    }

    @Test
    void doubledQuoteInsideQuotedFieldIsOneQuote() {
        assertArrayEquals(new String[]{"say \"hi\""}, FieldSplitter.split("\"say \"\"hi\"\"\""));
    }

    @Test
    void unclosedQuoteIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> FieldSplitter.split("a;\"b;c"));
    }

    @Test
    void characterAfterClosingQuoteIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> FieldSplitter.split("\"a\"b;c"));
    }
}