package org.example.sortingApplication.strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.example.sortingApplication.domain.Bus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EvenTimSortStrategyTest {

    private EvenTimSortStrategy mileageSorter;
    private EvenTimSortStrategy busNumberSorter;
    private Bus busEven1, busEven2, busOdd1, busOdd2, busEven3, busOdd3;

    @BeforeEach
    void setUp() {
        // Компаратор по пробегу
        Comparator<Bus> mileageComparator = Comparator.comparingInt(Bus::getMileage);
        mileageSorter = new EvenTimSortStrategy(mileageComparator);

        // Компаратор по номеру автобуса
        Comparator<Bus> busNumberComparator = Comparator.comparing(Bus::getBusNumber);
        busNumberSorter = new EvenTimSortStrategy(busNumberComparator);

        // Тестовые данные: четные и нечетные пробеги
        busEven1 = new Bus("C", "Mercedes", 40000);  // четный
        busEven2 = new Bus("A", "Volvo", 20000);     // четный
        busEven3 = new Bus("E", "BMW", 60000);       // четный
        busOdd1 = new Bus("B", "Audi", 30000);       // нечетный
        busOdd2 = new Bus("D", "Toyota", 50000);     // нечетный
        busOdd3 = new Bus("F", "Ford", 10000);       // нечетный
    }

    @Test
    void testSort_NullArray() {
        Bus[] result = mileageSorter.sort(null);
        assertNull(result);
    }

    @Test
    void testSort_EmptyArray() {
        Bus[] emptyArray = {};
        Bus[] result = mileageSorter.sort(emptyArray);
        assertEquals(0, result.length);
    }

    @Test
    void testSort_OnlyEvenMileageBuses() {
        Bus[] onlyEvenArray = {busEven1, busEven2, busEven3}; // все четные
        Bus[] result = mileageSorter.sort(onlyEvenArray);

        // Все должны быть отсортированы по пробегу
        assertEquals(20000, result[0].getMileage()); // busEven2
        assertEquals(40000, result[1].getMileage()); // busEven1
        assertEquals(60000, result[2].getMileage()); // busEven3
    }

    @Test
    void testSort_OnlyOddMileageBuses() {
        Bus[] onlyOddArray = {busOdd1, busOdd2, busOdd3}; // все нечетные
        Bus[] originalOrder = onlyOddArray.clone();
        Bus[] result = mileageSorter.sort(onlyOddArray);

        // Нечетные не должны сортироваться - порядок должен сохраниться
        assertArrayEquals(originalOrder, result);
    }

    @Test
    void testSort_MixedEvenAndOddBuses() {
        // Смешанный массив: четные и нечетные
        Bus[] mixedArray = {busEven1, busOdd1, busEven2, busOdd2, busEven3, busOdd3};
        Bus[] result = mileageSorter.sort(mixedArray);

        // Проверяем что четные отсортированы, а нечетные остались на своих местах
        assertEquals(20000, result[2].getMileage()); // busEven2 теперь на позиции 2
        assertEquals(40000, result[0].getMileage()); // busEven1 остается на позиции 0
        assertEquals(60000, result[4].getMileage()); // busEven3 остается на позиции 4

        // Проверяем что нечетные остались на тех же позициях
        assertSame(busOdd1, result[1]); // позиция 1
        assertSame(busOdd2, result[3]); // позиция 3
        assertSame(busOdd3, result[5]); // позиция 5
    }

    @Test
    void testSort_EvenBusesSortedByBusNumber() {
        Bus[] mixedArray = {busEven1, busOdd1, busEven2}; // номера: "C", "B", "A"
        Bus[] result = busNumberSorter.sort(mixedArray);

        // Четные должны быть отсортированы по номеру
        assertEquals("A", result[2].getBusNumber()); // busEven2 теперь "A"
        assertEquals("C", result[0].getBusNumber()); // busEven1 остается "C"

        // Нечетный остается на месте
        assertSame(busOdd1, result[1]);
    }

    @Test
    void testSort_PositionsPreservedForOddBuses() {
        Bus[] array = {
                busOdd1,  // позиция 0 - нечетный
                busEven1, // позиция 1 - четный
                busOdd2,  // позиция 2 - нечетный
                busEven2, // позиция 3 - четный
                busOdd3   // позиция 4 - нечетный
        };

        Bus[] result = mileageSorter.sort(array);

        // Проверяем что нечетные остались на своих исходных позициях
        assertSame(busOdd1, result[0]);
        assertSame(busOdd2, result[2]);
        assertSame(busOdd3, result[4]);

        // Проверяем что четные отсортированы по пробегу
        assertEquals(20000, result[3].getMileage()); // busEven2
        assertEquals(40000, result[1].getMileage()); // busEven1
    }

    @Test
    void testSort_WithNullElements() {
        Bus busNull = null;
        Bus[] arrayWithNulls = {busEven1, busNull, busEven2, busOdd1};
        Bus[] result = mileageSorter.sort(arrayWithNulls);

        // null должен игнорироваться (не считается четным)
        assertNull(result[1]);
        assertSame(busOdd1, result[3]);

        // Четные должны быть отсортированы
        assertEquals(20000, result[2].getMileage()); // busEven2
        assertEquals(40000, result[0].getMileage()); // busEven1
    }

    @Test
    void testSort_AllBusesSameMileageParity() {
        // Все автобусы с одинаковой четностью пробега
        Bus busEven4 = new Bus("G", "Nissan", 80000);
        Bus[] allEven = {busEven1, busEven2, busEven3, busEven4};
        Bus[] result = mileageSorter.sort(allEven);

        // Все должны быть отсортированы
        assertEquals(20000, result[0].getMileage());
        assertEquals(40000, result[1].getMileage());
        assertEquals(60000, result[2].getMileage());
        assertEquals(80000, result[3].getMileage());
    }

    @Test
    void testSort_SingleEvenBus() {
        Bus[] singleEven = {busEven1};
        Bus[] result = mileageSorter.sort(singleEven);

        assertEquals(1, result.length);
        assertSame(busEven1, result[0]);
    }

    @Test
    void testSort_SingleOddBus() {
        Bus[] singleOdd = {busOdd1};
        Bus[] result = mileageSorter.sort(singleOdd);

        assertEquals(1, result.length);
        assertSame(busOdd1, result[0]);
    }

    @Test
    void testSort_LargeMixedArray() {
        // Большой массив со смешанными четными/нечетными
        int size = 50;
        Bus[] largeArray = new Bus[size];
        List<Integer> evenPositions = new ArrayList<>();
        List<Integer> oddPositions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int mileage = i * 1000;
            largeArray[i] = new Bus("BUS" + i, "Model" + i, mileage);

            if (mileage % 2 == 0) {
                evenPositions.add(i);
            } else {
                oddPositions.add(i);
            }
        }

        Bus[] result = mileageSorter.sort(largeArray);

        // Проверяем что нечетные остались на своих местах
        for (int pos : oddPositions) {
            assertEquals(pos * 1000, result[pos].getMileage());
        }

        // Проверяем что четные отсортированы по пробегу
        int expectedEvenMileage = 0;
        for (int pos : evenPositions) {
            assertEquals(expectedEvenMileage, result[pos].getMileage());
            expectedEvenMileage += 2000; // только четные числа
        }
    }

    @Test
    void testGetDescription() {
        assertEquals("TimSort (только автобусы с четным пробегом сортируются)",
                mileageSorter.getDescription());
    }

    @Test
    void testSort_ZeroMileageBus() {
        // Пробег 0 считается четным
        Bus zeroMileageBus = new Bus("ZERO", "Zero", 0);
        Bus[] array = {busEven1, zeroMileageBus, busOdd1};
        Bus[] result = mileageSorter.sort(array);

        // Ноль должен быть отсортирован как наименьший четный
        assertEquals(0, result[1].getMileage()); // zeroMileageBus
        assertEquals(40000, result[0].getMileage()); // busEven1
        assertSame(busOdd1, result[2]); // нечетный на месте
    }

    @Test
    void testSort_NegativeMileage() {
        // Отрицательные пробеги (если такое возможно в логике)
        Bus negativeEven = new Bus("NEG", "Negative", -2000); // четный
        Bus negativeOdd = new Bus("NEG2", "Negative2", -3000); // нечетный

        Bus[] array = {busEven1, negativeEven, busOdd1, negativeOdd};
        Bus[] result = mileageSorter.sort(array);

        // Четные должны быть отсортированы (включая отрицательные)
        assertEquals(-2000, result[1].getMileage()); // negativeEven
        assertEquals(40000, result[0].getMileage()); // busEven1

        // Нечетные остаются на местах
        assertSame(busOdd1, result[2]);
        assertSame(negativeOdd, result[3]);
    }
}