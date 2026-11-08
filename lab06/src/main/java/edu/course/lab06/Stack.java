package edu.course.lab06;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Учебный стек (последним пришел, первым ушел). Вершина стека хранится в конце списка,
 * поэтому push, pop, peek, size и isEmpty работают за O(1) (push в среднем).
 */
public final class Stack<T> {

    private final List<T> items = new ArrayList<>();

    public void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
        items.add(value);
    }

    public T pop() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("stack is empty");
        }
        return items.remove(items.size() - 1);
    }

    public T peek() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("stack is empty");
        }
        return items.get(items.size() - 1);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
