package edu.course.lab06;

/**
 * Коробка с одним значением любого ссылочного типа T.
 */
public final class Box<T> {

    private T value;

    public Box(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
