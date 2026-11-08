package edu.course.lab06;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

    @Test
    void newStackIsEmpty() {
        Stack<String> stack = new Stack<>();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    void pushIncreasesSizeAndMakesStackNotEmpty() {
        Stack<String> stack = new Stack<>();
        stack.push("a");
        stack.push("b");
        assertFalse(stack.isEmpty());
        assertEquals(2, stack.size());
    }

    @Test
    void pushRejectsNull() {
        Stack<String> stack = new Stack<>();
        assertThrows(IllegalArgumentException.class, () -> stack.push(null));
        assertTrue(stack.isEmpty());
    }

    @Test
    void peekReturnsTopWithoutRemoving() {
        Stack<String> stack = new Stack<>();
        stack.push("a");
        stack.push("b");
        assertEquals("b", stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    void peekOnEmptyStackThrows() {
        Stack<String> stack = new Stack<>();
        assertThrows(NoSuchElementException.class, stack::peek);
    }

    @Test
    void popReturnsElementsInReverseOrder() {
        Stack<String> stack = new Stack<>();
        stack.push("a");
        stack.push("b");
        stack.push("c");
        assertEquals("c", stack.pop());
        assertEquals("b", stack.pop());
        assertEquals("a", stack.pop());
    }

    @Test
    void popRemovesElement() {
        Stack<String> stack = new Stack<>();
        stack.push("a");
        stack.pop();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    void popOnEmptyStackThrows() {
        Stack<String> stack = new Stack<>();
        assertThrows(NoSuchElementException.class, stack::pop);
    }

    @Test
    void worksWithStrings() {
        Stack<String> stack = new Stack<>();
        stack.push("text");
        String top = stack.pop();
        assertEquals("text", top);
    }

    @Test
    void worksWithIntegers() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        int top = stack.pop();
        assertEquals(20, top);
        assertEquals(Integer.valueOf(10), stack.peek());
    }
}
