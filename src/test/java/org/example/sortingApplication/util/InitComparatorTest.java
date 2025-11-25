package org.example.sortingApplication.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.example.sortingApplication.domain.Bus;

class InitComparatorTest {

    private InitComparator initComparator;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        initComparator = new InitComparator();
        System.setOut(new PrintStream(outputStream));
    }

    private String getConsoleOutput() {
        return outputStream.toString();
    }

    private Scanner createScannerWithInput(String input) {
        // Для двойного nextLine() нужно добавить пустую строку перед реальным вводом
        return new Scanner("\n" + input);
    }

    @Test
    @DisplayName("Корректная инициализация компаратора с порядком 1 2 3")
    void testInit_ValidOrder123() {
        Scanner scanner = createScannerWithInput("1 2 3");

        BusComparator comparator = initComparator.init(scanner);

        assertNotNull(comparator);
        assertTrue(getConsoleOutput().contains("Выберите в каком порядке сортировать автобусы"));
    }

    @Test
    @DisplayName("Корректная инициализация компаратора с порядком 3 2 1")
    void testInit_ValidOrder321() {
        Scanner scanner = createScannerWithInput("3 2 1");

        BusComparator comparator = initComparator.init(scanner);

        assertNotNull(comparator);
    }

    @Test
    @DisplayName("Корректная инициализация с дополнительными пробелами")
    void testInit_ValidOrderWithExtraSpaces() {
        Scanner scanner = createScannerWithInput("  1   2   3  ");

        BusComparator comparator = initComparator.init(scanner);

        assertNotNull(comparator);
    }

    @Test
    @DisplayName("Только пробелы - должен вернуть null")
    void testInit_OnlySpaces() {
        Scanner scanner = createScannerWithInput("   ");

        BusComparator result = initComparator.init(scanner);

        assertNull(result);
        assertTrue(getConsoleOutput().contains("Не введен порядок сортировки"));
    }

    @Test
    @DisplayName("Некорректные числа - должен вернуть null")
    void testInit_InvalidNumbers() {
        Scanner scanner = createScannerWithInput("1 4 3");

        BusComparator result = initComparator.init(scanner);

        assertNull(result);
        assertTrue(getConsoleOutput().contains("Можно использовать только числа 1, 2, 3"));
    }

    @Test
    @DisplayName("Буквы вместо чисел - должен вернуть null")
    void testInit_LettersInsteadOfNumbers() {
        Scanner scanner = createScannerWithInput("1 a 3");

        BusComparator result = initComparator.init(scanner);

        assertNull(result);
        assertTrue(getConsoleOutput().contains("Можно использовать только числа 1, 2, 3"));
    }

    @Test
    @DisplayName("Больше 3 чисел - должен вернуть null")
    void testInit_MoreThanThreeNumbers() {
        Scanner scanner = createScannerWithInput("1 2 3 4");

        BusComparator result = initComparator.init(scanner);

        assertNull(result);
        assertTrue(getConsoleOutput().contains("Нельзя вводить больше 3 чисел"));
    }

    @Test
    @DisplayName("Меньше 3 чисел - должен создать компаратор")
    void testInit_LessThanThreeNumbers() {
        Scanner scanner = createScannerWithInput("1 2");

        BusComparator result = initComparator.init(scanner);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Одно число - должен создать компаратор")
    void testInit_SingleNumber() {
        Scanner scanner = createScannerWithInput("1");

        BusComparator comparator = initComparator.init(scanner);

        assertNotNull(comparator);
    }

    @Test
    @DisplayName("Два числа - должен создать компаратор")
    void testInit_TwoNumbers() {
        Scanner scanner = createScannerWithInput("1 2");

        BusComparator comparator = initComparator.init(scanner);

        assertNotNull(comparator);
    }

    @Test
    @DisplayName("Дублирующиеся числа - должен создать компаратор")
    void testInit_DuplicateNumbers() {
        Scanner scanner = createScannerWithInput("1 1 2");

        BusComparator comparator = initComparator.init(scanner);

        assertNotNull(comparator);
    }

    @Test
    @DisplayName("Проверка корректности маппинга чисел на поля")
    void testInit_CorrectFieldMapping() {
        Scanner scanner = createScannerWithInput("1 2 3");

        BusComparator comparator = initComparator.init(scanner);

        // Создаем тестовые автобусы для проверки логики компаратора
        Bus bus1 = new Bus("B", "ModelA", 10000);
        Bus bus2 = new Bus("A", "ModelB", 20000);

        // При порядке "1 2 3" сначала сравниваем по номеру
        // "A" < "B", поэтому bus2 должен быть меньше bus1
        assertTrue(comparator.compare(bus2, bus1) < 0);
    }

    @Test
    @DisplayName("Проверка маппинга для разных комбинаций")
    void testInit_FieldMappingCombinations() {
        // Тест для комбинации 2 1 3 (модель -> номер -> пробег)
        Scanner scanner = createScannerWithInput("2 1 3");

        BusComparator comparator = initComparator.init(scanner);

        Bus bus1 = new Bus("B", "Alpha", 10000);  // Model Alpha
        Bus bus2 = new Bus("A", "Beta", 20000);   // Model Beta

        // Alpha < Beta, поэтому bus1 должен быть меньше bus2
        assertTrue(comparator.compare(bus1, bus2) < 0);
    }

    @Test
    @DisplayName("Проверка что не падает при null в массиве (если введено меньше 3 чисел)")
    void testInit_WithNullFieldsInArray() {
        Scanner scanner = createScannerWithInput("1"); // Только одно число

        BusComparator comparator = initComparator.init(scanner);

        // Компаратор должен создаться без NPE, даже если некоторые поля null
        assertNotNull(comparator);

        // Проверяем что компаратор работает без исключений
        Bus bus1 = new Bus("A", "Model", 10000);
        Bus bus2 = new Bus("B", "Model", 10000);
        assertDoesNotThrow(() -> comparator.compare(bus1, bus2));
    }
}