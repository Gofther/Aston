package org.example.sortingApplication.domain;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

public class BusCollection implements Iterable<Bus> {
    private Bus[] buses;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public BusCollection() {
        buses = new Bus[DEFAULT_CAPACITY];
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
        return buses[index];
    }

    public int size() { return size; }

    private void resize() {
        Bus[] newArray = new Bus[buses.length * 2];
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
        return Arrays.stream(buses, 0, size);
    }

    public Bus[] toArray() { //нужен потому что поу молчанию размер массива 10, если элементов будет, то остальные будут null, что ломает сортировку
        return Arrays.copyOf(buses, size);//копируются только не пустые элементы
    }

    public void setBuses(Bus[] buses) {
        this.buses = buses;
    }
}

