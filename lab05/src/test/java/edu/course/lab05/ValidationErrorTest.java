package edu.course.lab05;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationErrorTest {

    @Test
    void storesLineFieldAndMessage() {
        ValidationError error = new ValidationError(3, "status", "unknown status DONE");
        assertEquals(3, error.getLineNumber());
        assertEquals("status", error.getField());
        assertEquals("unknown status DONE", error.getMessage());
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new ValidationError(0, "id", "m"));
        assertThrows(IllegalArgumentException.class, () -> new ValidationError(1, " ", "m"));
        assertThrows(IllegalArgumentException.class, () -> new ValidationError(1, "id", null));
    }
}