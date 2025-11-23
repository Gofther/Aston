package org.example.sortingApplication.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class BusCollection implements Iterable<Bus> {
    private Object[] buses;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public BusCollection() {
        buses = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void addAll(BusCollection collection) {
        buses = collection.buses;
        size = collection.size;
    }

    public void add(Bus bus) {
        if (size == buses.length) {
            resize();
        }
        buses[size++] = bus;
    }

    public Bus get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (Bus) buses[index];
    }

    public int size() { return size; }

    private void resize() {
        Object[] newArray = new Object[buses.length * 2];
        System.arraycopy(buses, 0, newArray, 0, size);
        buses = newArray;
    }

    @Override
    public Iterator<Bus> iterator() {
        return new Iterator<Bus>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public Bus next() {
                return get(currentIndex++);
            }
        };
    }

    // Stream support (Доп. задание 3)
    public Stream<Bus> stream() {
        List<Bus> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(get(i));
        }
        return list.stream();
    }
}

