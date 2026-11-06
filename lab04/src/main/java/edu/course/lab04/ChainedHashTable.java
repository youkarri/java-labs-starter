package edu.course.lab04;

public final class ChainedHashTable {

    private static final class Node {
        private final SampleId key;
        private DataSample value;
        private Node next;

        private Node(SampleId key, DataSample value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private final Node[] buckets;
    private int size;

    public ChainedHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be positive");
        }
        this.buckets = new Node[bucketCount];
    }

    private int indexOf(SampleId key) {
        return (key.hashCode() & 0x7fffffff) % buckets.length;
    }

    public void put(SampleId key, DataSample value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key and value must not be null");
        }
        int index = indexOf(key);
        for (Node node = buckets[index]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        buckets[index] = new Node(key, value, buckets[index]);
        size++;
    }

    public DataSample get(SampleId key) {
        if (key == null) {
            return null;
        }
        for (Node node = buckets[indexOf(key)]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    public boolean containsKey(SampleId key) {
        return get(key) != null;
    }

    public DataSample remove(SampleId key) {
        if (key == null) {
            return null;
        }
        int index = indexOf(key);
        Node previous = null;
        for (Node node = buckets[index]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = node.next;
                } else {
                    previous.next = node.next;
                }
                size--;
                return node.value;
            }
            previous = node;
        }
        return null;
    }

    public int size() {
        return size;
    }
}