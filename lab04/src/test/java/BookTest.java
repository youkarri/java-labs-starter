package edu.course.lab04;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void booksWithSameIsbnAreEqual() {
        Book first = new Book("978-1", "Java");
        Book second = new Book("978-1", "Java (2nd edition)");
        assertTrue(first.equals(second));
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void booksWithDifferentIsbnAreNotEqual() {
        Book first = new Book("978-1", "Java");
        Book second = new Book("978-2", "Java");
        assertFalse(first.equals(second));
    }

    @Test
    void mapFindsValueByAnotherInstanceWithSameIsbn() {
        Map<Book, String> shelves = new HashMap<>();
        shelves.put(new Book("978-1", "Java"), "shelf A");
        assertEquals("shelf A", shelves.get(new Book("978-1", "Java")));
    }

    @Test
    void comparisonWithNullAndOtherTypeIsFalse() {
        Book book = new Book("978-1", "Java");
        assertFalse(book.equals(null));
        assertFalse(book.equals("978-1"));
    }

    @Test
    void rejectsBlankIsbn() {
        assertThrows(IllegalArgumentException.class, () -> new Book(" ", "Java"));
    }
}