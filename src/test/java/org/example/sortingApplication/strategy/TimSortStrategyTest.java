package org.example.sortingApplication.strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.example.sortingApplication.util.BusComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Comparator;
import org.example.sortingApplication.domain.Bus;

class TimSortStrategyTest {

    private TimSortStrategy busNumberSorter;
    private TimSortStrategy mileageSorter;
    private TimSortStrategy modelSorter;
    private Bus bus1, bus2, bus3, bus4, bus5;

    @BeforeEach
    void setUp() {
        // Компаратор по номеру автобуса
        Comparator<Bus> busNumberComparator = Comparator.comparing(Bus::getBusNumber);
        busNumberSorter = new TimSortStrategy(busNumberComparator);

        // Компаратор по пробегу
        Comparator<Bus> mileageComparator = Comparator.comparingInt(Bus::getMileage);
        mileageSorter = new TimSortStrategy(mileageComparator);

        // Компаратор по модели
        Comparator<Bus> modelComparator = Comparator.comparing(Bus::getModel);
        modelSorter = new TimSortStrategy(modelComparator);

        // Тестовые данные
        bus1 = new Bus("C", "Mercedes", 30000);
        bus2 = new Bus("A", "Volvo", 10000);
        bus3 = new Bus("B", "BMW", 20000);
        bus4 = new Bus("D", "Audi", 50000);
        bus5 = new Bus("E", "Toyota", 40000);
    }

    @Test
    void testSort_NullArray() {
        Bus[] result = busNumberSorter.sort(null);
        assertNull(result);
    }

    @Test
    void testSort_EmptyArray() {
        Bus[] emptyArray = {};
        Bus[] result = busNumberSorter.sort(emptyArray);
        assertEquals(0, result.length);
    }

    @Test
    void testSort_SingleElement() {
        Bus[] singleArray = {bus1};
        Bus[] result = busNumberSorter.sort(singleArray);

        assertEquals(1, result.length);
        assertSame(bus1, result[0]);
    }

    @Test
    void testSort_AlreadySortedByBusNumber() {
        Bus[] sortedArray = {bus2, bus3, bus1}; // номера: "A", "B", "C"
        Bus[] result = busNumberSorter.sort(sortedArray);

        // Должен остаться отсортированным
        assertEquals(bus2, result[0]); // "A"
        assertEquals(bus3, result[1]); // "B"
        assertEquals(bus1, result[2]); // "C"
    }

    @Test
    void testSort_ReverseSortedByBusNumber() {
        Bus[] reverseArray = {bus1, bus3, bus2}; // номера: "C", "B", "A"
        Bus[] result = busNumberSorter.sort(reverseArray);

        // Должен стать отсортированным по алфавиту
        assertEquals(bus2, result[0]); // "A"
        assertEquals(bus3, result[1]); // "B"
        assertEquals(bus1, result[2]); // "C"
    }

    @Test
    void testSort_ByMileage() {
        Bus[] unsortedArray = {bus4, bus2, bus1, bus5, bus3}; // пробеги: 50000, 10000, 30000, 40000, 20000
        Bus[] result = mileageSorter.sort(unsortedArray);

        // Проверяем сортировку по возрастанию пробега
        assertEquals(10000, result[0].getMileage()); // bus2
        assertEquals(20000, result[1].getMileage()); // bus3
        assertEquals(30000, result[2].getMileage()); // bus1
        assertEquals(40000, result[3].getMileage()); // bus5
        assertEquals(50000, result[4].getMileage()); // bus4
    }

    @Test
    void testSort_ByModel() {
        Bus[] unsortedArray = {bus1, bus2, bus3, bus4, bus5}; // модели: Mercedes, Volvo, BMW, Audi, Toyota
        Bus[] result = modelSorter.sort(unsortedArray);

        // Должен быть отсортирован по алфавиту моделей
        assertEquals("Audi", result[0].getModel());    // bus4
        assertEquals("BMW", result[1].getModel());     // bus3
        assertEquals("Mercedes", result[2].getModel()); // bus1
        assertEquals("Toyota", result[3].getModel());   // bus5
        assertEquals("Volvo", result[4].getModel());    // bus2
    }

    @Test
    void testSort_LargeArrayByMileage() {
        // Создаем массив больше MIN_MERGE (32) чтобы проверить слияние
        Bus[] largeArray = new Bus[50];
        for (int i = 0; i < 50; i++) {
            largeArray[i] = new Bus("BUS" + i, "Driver" + i, 49 - i); // обратный порядок пробега
        }

        Bus[] result = mileageSorter.sort(largeArray);

        // Проверяем что отсортировано по возрастанию пробега
        for (int i = 0; i < 50; i++) {
            assertEquals(i, result[i].getMileage());
        }
    }

    @Test
    void testSort_ArraySizeExactlyMinMerge() {
        // Массив размером точно MIN_MERGE
        Bus[] array = new Bus[32];
        for (int i = 0; i < 32; i++) {
            array[i] = new Bus("BUS" + i, "Model" + i, 31 - i); // обратный порядок
        }

        Bus[] result = mileageSorter.sort(array);

        for (int i = 0; i < 32; i++) {
            assertEquals(i, result[i].getMileage());
        }
    }

    @Test
    void testSort_WithDuplicateMileage() {
        Bus bus6 = new Bus("F", "Ford", 20000); // тот же пробег что у bus3
        Bus[] array = {bus1, bus2, bus3, bus6}; // пробеги: 30000, 10000, 20000, 20000

        Bus[] result = mileageSorter.sort(array);

        // Должны быть отсортированы по пробегу
        assertEquals(10000, result[0].getMileage()); // bus2
        assertTrue(result[1].getMileage() == 20000); // bus3 или bus6
        assertTrue(result[2].getMileage() == 20000); // bus3 или bus6
        assertEquals(30000, result[3].getMileage()); // bus1
    }

    @Test
    void testSort_StabilityCheck() {
        // Тест на стабильность сортировки
        Bus bus6 = new Bus("A", "Volvo", 15000); // тот же номер что у bus2, но другой пробег
        Bus[] array = {bus1, bus2, bus6}; // номера: "C", "A", "A"

        // Компаратор по номеру автобуса
        Bus[] result = busNumberSorter.sort(array);

        // Оба с номером "A" должны сохранить относительный порядок
        // (стабильность TimSort)
        assertEquals("A", result[0].getBusNumber());
        assertEquals("A", result[1].getBusNumber());
        assertEquals("C", result[2].getBusNumber());

        // Проверяем что bus2 и bus6 сохранили исходный порядок
        // (так как они были равны по компаратору)
        assertSame(bus2, result[0]);
        assertSame(bus6, result[1]);
    }

    @Test
    void testMergeMethod() {
        // Прямой тест метода merge
        Bus[] array = new Bus[4];
        // Левая часть: bus2 (10000), bus3 (20000)
        // Правая часть: bus1 (30000), bus4 (50000)
        Bus[] sortedLeft = {bus2, bus3}; // 10000, 20000
        Bus[] sortedRight = {bus1, bus4}; // 30000, 50000

        // Копируем отсортированные части в массив
        System.arraycopy(sortedLeft, 0, array, 0, 2);
        System.arraycopy(sortedRight, 0, array, 2, 2);

        // Вызываем merge напрямую
        mileageSorter.merge(array, 0, 1, 3);

        // Проверяем что слияние работает
        assertEquals(10000, array[0].getMileage()); // bus2
        assertEquals(20000, array[1].getMileage()); // bus3
        assertEquals(30000, array[2].getMileage()); // bus1
        assertEquals(50000, array[3].getMileage()); // bus4
    }

    @Test
    void testInsertionSortMethod() {
        // Прямой тест метода insertionSort
        Bus[] array = {bus3, bus1, bus2}; // пробеги: 20000, 30000, 10000

        // Сортируем вставками диапазон [0, 2]
        mileageSorter.insertionSort(array, 0, 2);

        // Проверяем что диапазон отсортирован
        assertEquals(10000, array[0].getMileage()); // bus2
        assertEquals(20000, array[1].getMileage()); // bus3
        assertEquals(30000, array[2].getMileage()); // bus1
    }

    @Test
    void testGetDescription() {
        assertEquals("TimSort Algorithm (гибридная сортировка слиянием и вставками)",
                busNumberSorter.getDescription());
    }

    @Test
    void testSort_PerformanceWithLargeArray() {
        // Тест производительности на большом массиве
        int size = 1000;
        Bus[] largeArray = new Bus[size];
        for (int i = 0; i < size; i++) {
            largeArray[i] = new Bus("BUS" + i, "Model" + i, size - 1 - i); // обратный порядок
        }

        long startTime = System.nanoTime();
        Bus[] result = mileageSorter.sort(largeArray);
        long endTime = System.nanoTime();

        // Проверяем корректность сортировки
        for (int i = 0; i < size; i++) {
            assertEquals(i, result[i].getMileage());
        }

        // Проверяем что сортировка выполняется за разумное время
        long duration = endTime - startTime;
        assertTrue(duration < 1_000_000_000, "Sorting should complete within 1 second");
    }

    @Test
    void testSort_WithCustomBusComparator() {
        // Тест с твоим BusComparator для многоуровневой сортировки
        BusComparator customComparator = new BusComparator("пробег", "номер", "модель");
        TimSortStrategy customSorter = new TimSortStrategy(customComparator);

        Bus bus6 = new Bus("B", "BMW", 20000);    // тот же пробег что у bus3, но другой номер
        Bus[] array = {bus1, bus2, bus3, bus6};   // пробеги: 30000, 10000, 20000, 20000

        Bus[] result = customSorter.sort(array);

        // Сначала по пробегу, затем по номеру для одинаковых пробегов
        assertEquals(10000, result[0].getMileage()); // bus2
        assertEquals(20000, result[1].getMileage()); // bus3 или bus6
        assertEquals(20000, result[2].getMileage()); // bus3 или bus6
        assertEquals(30000, result[3].getMileage()); // bus1
    }
}