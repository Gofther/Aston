package org.example.sortingApplication.util;

import static org.junit.jupiter.api.Assertions.*;

import org.example.sortingApplication.domain.Bus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Comparator;

class BusComparatorTest {

    private Bus bus1, bus2, bus3, bus4;

    @BeforeEach
    void setUp() {
        bus1 = new Bus("ABC123", "Mercedes", 50000);
        bus2 = new Bus("DEF456", "Volvo", 30000);
        bus3 = new Bus("ABC123", "Volvo", 40000); // такой же номер как bus1, но другая модель
        bus4 = new Bus("abc123", "Mercedes", 50000); // тот же номер в нижнем регистре
    }

    // Тесты для сортировки по одному полю
    @Test
    void testCompare_PrimaryFieldOnly() {
        Comparator<Bus> comparator = new BusComparator("номер", "номер", "номер");

        // ABC123 < DEF456
        assertTrue(comparator.compare(bus1, bus2) < 0);
        assertTrue(comparator.compare(bus2, bus1) > 0);

        // ABC123 == ABC123
        assertEquals(0, comparator.compare(bus1, bus3));
    }

    @Test
    void testCompare_ByMileage() {
        Comparator<Bus> comparator = new BusComparator("пробег", "пробег", "пробег");

        // 30000 < 50000
        assertTrue(comparator.compare(bus2, bus1) < 0);
        assertTrue(comparator.compare(bus1, bus2) > 0);
    }

    // Тесты для многоуровневой сортировки
    @Test
    void testCompare_MultiLevelSorting() {
        // Сначала по номеру, потом по модели, потом по пробегу
        Comparator<Bus> comparator = new BusComparator("номер", "модель", "пробег");

        // bus1 и bus3 имеют одинаковый номер "ABC123", но разные модели
        // "Mercedes" < "Volvo" (M перед V в алфавите)
        assertTrue(comparator.compare(bus1, bus3) < 0);
        assertTrue(comparator.compare(bus3, bus1) > 0);
    }

    @Test
    void testCompare_ThreeLevelSorting() {
        Bus bus5 = new Bus("ABC123", "Mercedes", 60000); // тот же номер и модель, но другой пробег

        Comparator<Bus> comparator = new BusComparator("номер", "модель", "пробег");

        // bus1 и bus5 имеют одинаковые номер и модель, но разный пробег
        // 50000 < 60000
        assertTrue(comparator.compare(bus1, bus5) < 0);
        assertTrue(comparator.compare(bus5, bus1) > 0);
    }

    // Тесты для чувствительности к регистру
    @Test
    void testCompare_CaseSensitive() {
        Comparator<Bus> caseSensitiveComparator = new BusComparator("номер", "номер", "номер", true);

        // "ABC123" != "abc123" (с учетом регистра)
        assertTrue(caseSensitiveComparator.compare(bus1, bus4) < 0); // A < a в ASCII
        assertTrue(caseSensitiveComparator.compare(bus4, bus1) > 0);
    }

    @Test
    void testCompare_CaseInsensitive() {
        Comparator<Bus> caseInsensitiveComparator = new BusComparator("номер", "номер", "номер", false);

        // "ABC123" == "abc123" (без учета регистра)
        assertEquals(0, caseInsensitiveComparator.compare(bus1, bus4));
    }

    // Тесты граничных случаев
    @Test
    void testCompare_EqualBuses() {
        Bus bus1Copy = new Bus("ABC123", "Mercedes", 50000);
        Comparator<Bus> comparator = new BusComparator("номер", "модель", "пробег");

        assertEquals(0, comparator.compare(bus1, bus1Copy));
    }



    // Тест на неизвестное поле
    @Test
    void testCompare_UnknownField() {
        Comparator<Bus> comparator = new BusComparator("неизвестное поле", "номер", "модель");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> comparator.compare(bus1, bus2)
        );

        assertTrue(exception.getMessage().contains("Unknown field"));
    }

    // Тесты для разных комбинаций полей
    @Test
    void testCompare_DifferentFieldCombinations() {
        // Комбинация 1: номер -> пробег -> модель
        Comparator<Bus> comp1 = new BusComparator("номер", "пробег", "модель");

        // Комбинация 2: модель -> номер -> пробег
        Comparator<Bus> comp2 = new BusComparator("модель", "номер", "пробег");

        Bus testBus1 = new Bus("B", "ModelA", 10000);
        Bus testBus2 = new Bus("A", "ModelB", 20000);

        // Для comp1: "B" > "A" - положительное число
        assertTrue(comp1.compare(testBus1, testBus2) > 0);

        // Для comp2: "ModelA" < "ModelB" - отрицательное число
        assertTrue(comp2.compare(testBus1, testBus2) < 0);
    }

    @Test
    void testCompare_AllFieldsEqual() {
        Bus identical1 = new Bus("ABC123", "Mercedes", 50000);
        Bus identical2 = new Bus("ABC123", "Mercedes", 50000);

        Comparator<Bus> comparator = new BusComparator("номер", "модель", "пробег");

        assertEquals(0, comparator.compare(identical1, identical2));
    }
}