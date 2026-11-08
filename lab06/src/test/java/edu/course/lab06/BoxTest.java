package edu.course.lab06;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoxTest {

    // Параметр типа защищает от ошибок типов на этапе компиляции.
    // Следующая строка не компилируется, потому что int нельзя передать в Box<String>.
    // Поэтому она оставлена только в комментарии.
    // Box<String> wrong = new Box<>(42);

    @Test
    void stringBoxReturnsValue() {
        Box<String> box = new Box<>("hello");
        assertEquals("hello", box.get());
    }

    @Test
    void stringBoxValueCanBeChanged() {
        Box<String> box = new Box<>("hello");
        box.set("world");
        assertEquals("world", box.get());
    }

    @Test
    void integerBoxReturnsValue() {
        Box<Integer> box = new Box<>(42);
        int value = box.get();
        assertEquals(42, value);
    }

    @Test
    void integerBoxValueCanBeChanged() {
        Box<Integer> box = new Box<>(1);
        box.set(2);
        assertEquals(Integer.valueOf(2), box.get());
    }
}
