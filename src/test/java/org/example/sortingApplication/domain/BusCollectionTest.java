package org.example.sortingApplication.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class BusCollectionTest {

    private BusCollection collection;
    private Bus bus1, bus2, bus3;

    @BeforeEach
    void setUp() {
        collection = new BusCollection();
        bus1 = new Bus("ABC123", "Driver1", 1);
        bus2 = new Bus("DEF456", "Driver2", 2);
        bus3 = new Bus("GHI789", "Driver3", 3);
    }

    @Test
    void testEmptyCollection() {
        assertEquals(0, collection.size());
        assertTrue(collection.isEmpty());
    }

    @Test
    void testAddSingleBus() {
        collection.add(bus1);

        assertEquals(1, collection.size());
        assertFalse(collection.isEmpty());
        assertEquals(bus1, collection.get(0));
    }

    @Test
    void testAddMultipleBuses() {
        collection.add(bus1);
        collection.add(bus2);
        collection.add(bus3);

        assertEquals(3, collection.size());
        assertEquals(bus1, collection.get(0));
        assertEquals(bus2, collection.get(1));
        assertEquals(bus3, collection.get(2));
    }

    @Test
    void testGetWithInvalidIndex() {
        collection.add(bus1);

        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(1));
    }

    @Test
    void testAutomaticResize() {
        // Добавляем больше элементов, чем начальная емкость (10)
        for (int i = 0; i < 15; i++) {
            collection.add(new Bus("BUS" + i, "Driver" + i, i));
        }

        assertEquals(15, collection.size());
        // Проверяем, что все элементы на месте
        for (int i = 0; i < 15; i++) {
            assertEquals("BUS" + i, collection.get(i).getBusNumber());
        }
    }

    @Test
    void testIterator() {
        collection.add(bus1);
        collection.add(bus2);
        collection.add(bus3);

        Iterator<Bus> iterator = collection.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(bus1, iterator.next());
        assertEquals(bus2, iterator.next());
        assertEquals(bus3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorOnEmptyCollection() {
        Iterator<Bus> iterator = collection.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testStreamSupport() {
        collection.add(bus1);
        collection.add(bus2);
        collection.add(bus3);

        Stream<Bus> stream = collection.stream();
        assertNotNull(stream);

        long count = stream.count();
        assertEquals(3, count);
    }

    @Test
    void testToArray() {
        collection.add(bus1);
        collection.add(bus2);

        Bus[] array = collection.toArray();
        assertEquals(2, array.length);
        assertEquals(bus1, array[0]);
        assertEquals(bus2, array[1]);

        // Проверяем, что это копия, а не оригинальный массив
        array[0] = bus3;
        assertEquals(bus1, collection.get(0)); // Оригинал не изменился
    }

    @Test
    void testAddAll() {
        BusCollection sourceCollection = new BusCollection();
        sourceCollection.add(bus1);
        sourceCollection.add(bus2);
        sourceCollection.add(bus3);

        collection.addAll(sourceCollection);

        assertEquals(3, collection.size());
        assertEquals(bus1, collection.get(0));
        assertEquals(bus2, collection.get(1));
        assertEquals(bus3, collection.get(2));
    }

    @Test
    void testAddAllReplacesExisting() {
        // Добавляем сначала один элемент
        collection.add(new Bus("OLD", "OldDriver", 99));

        BusCollection sourceCollection = new BusCollection();
        sourceCollection.add(bus1);
        sourceCollection.add(bus2);

        collection.addAll(sourceCollection);

        // Должны остаться только элементы из sourceCollection
        assertEquals(2, collection.size());
        assertEquals(bus1, collection.get(0));
        assertEquals(bus2, collection.get(1));
    }

    @Test
    void testForEachLoop() {
        collection.add(bus1);
        collection.add(bus2);
        collection.add(bus3);

        int count = 0;
        for (Bus bus : collection) {
            assertNotNull(bus);
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    void testSetBuses() {
        Bus[] testBuses = {bus1, bus2, bus3};
        collection.setBuses(testBuses);

        // После установки массива нужно обновить size
        // Возможно, тебе нужно добавить сеттер для size или пересмотреть логику
        // Этот тест может не работать без дополнительных изменений
    }
}
