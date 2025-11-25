package org.example.sortingApplication.Service;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.domain.BusCollection;
import org.example.sortingApplication.service.InputService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class InputServiceTest {
    private InputService inputService;
    private String testFilename;

    @BeforeEach
    void setUp() {
        inputService = new InputService();
        testFilename = "test_input.txt";

        // Удаляем тестовый файл перед каждым тестом
        try {
            Files.deleteIfExists(Paths.get(testFilename));
        } catch (IOException e) {
            // Игнорируем если файла нет
        }
    }

    @AfterEach
    void tearDown() {
        // Очищаем после каждого теста
        try {
            Files.deleteIfExists(Paths.get(testFilename));
        } catch (IOException e) {
            // Игнорируем
        }
    }

    private void createTestFileWithContent(List<String> content) throws IOException {
        Files.write(Paths.get(testFilename), content);
    }

    @Test
    @DisplayName("Создание коллекции из файла с валидными данными")
    void testCreateCollectionFromFile_ValidData() throws IOException {
        // Arrange - создаем файл с валидными данными
        List<String> fileContent = List.of(
                "A123,Mercedes,50000",
                "B456,Volvo,30000",
                "C789,BMW,40000"
        );
        createTestFileWithContent(fileContent);

        // Act
        BusCollection result = inputService.createCollectionFromFile(testFilename);

        // Assert
        assertNotNull(result, "Коллекция не должна быть null");
        assertEquals(3, result.size(), "Должно быть 3 автобуса в коллекции");

        // Проверяем содержимое
        Bus firstBus = result.get(0);
        assertEquals("A123", firstBus.getBusNumber());
        assertEquals("Mercedes", firstBus.getModel());
        assertEquals(50000, firstBus.getMileage());
    }

    @Test
    @DisplayName("Создание коллекции из файла с невалидными данными")
    void testCreateCollectionFromFile_InvalidData() throws IOException {
        // Arrange - создаем файл со смешанными валидными и невалидными данными
        List<String> fileContent = List.of(
                "A123,Mercedes,50000",      // валидный
                "Invalid,Data,-100",        // невалидный пробег
                "",                         // пустая строка
                "   ",                      // строка с пробелами
                "B456,Volvo,30000",         // валидный
                "OnlyTwoFields,BMW",        // недостаточно полей
                "C789,BMW,40000"           // валидный
        );
        createTestFileWithContent(fileContent);

        // Act
        BusCollection result = inputService.createCollectionFromFile(testFilename);

        // Assert - должны остаться только валидные автобусы
        assertNotNull(result);
        assertEquals(3, result.size(), "Должно быть 3 валидных автобуса");

        // Проверяем что только валидные данные попали в коллекцию
        assertTrue(result.stream().anyMatch(bus -> "A123".equals(bus.getBusNumber())));
        assertTrue(result.stream().anyMatch(bus -> "B456".equals(bus.getBusNumber())));
        assertTrue(result.stream().anyMatch(bus -> "C789".equals(bus.getBusNumber())));
    }

    @Test
    @DisplayName("Создание коллекции из несуществующего файла")
    void testCreateCollectionFromFile_NonExistentFile() {
        // Act
        BusCollection result = inputService.createCollectionFromFile("non_existent_file.txt");

        // Assert
        assertNotNull(result, "Коллекция не должна быть null");
        assertTrue(result.isEmpty(), "Коллекция должна быть пустой для несуществующего файла");
    }

    @Test
    @DisplayName("Создание коллекции из пустого файла")
    void testCreateCollectionFromFile_EmptyFile() throws IOException {
        // Arrange - создаем пустой файл
        Files.createFile(Paths.get(testFilename));

        // Act
        BusCollection result = inputService.createCollectionFromFile(testFilename);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Коллекция должна быть пустой для пустого файла");
    }

    @Test
    @DisplayName("Создание случайной коллекции с положительным размером")
    void testCreateCollectionRandom_PositiveSize() {
        // Act
        BusCollection result = inputService.createCollectionRandom(5);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size(), "Должно быть 5 автобусов в коллекции");

        // Проверяем что у всех автобусов заполнены поля
        for (int i = 0; i < result.size(); i++) {
            Bus bus = result.get(i);
            assertNotNull(bus.getBusNumber());
            assertNotNull(bus.getModel());
            assertTrue(bus.getMileage() >= 0, "Пробег не может быть отрицательным");
            assertTrue(bus.getMileage() <= 500000, "Пробег должен быть в разумных пределах");
        }
    }

    @Test
    @DisplayName("Создание случайной коллекции с нулевым размером")
    void testCreateCollectionRandom_ZeroSize() {
        // Act
        BusCollection result = inputService.createCollectionRandom(0);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Коллекция должна быть пустой при размере 0");
    }

    @Test
    @DisplayName("Создание случайной коллекции с большим размером")
    void testCreateCollectionRandom_LargeSize() {
        // Act
        BusCollection result = inputService.createCollectionRandom(1000);

        // Assert
        assertNotNull(result);
        assertEquals(1000, result.size(), "Должно быть 1000 автобусов в коллекции");

        // Проверяем что номера автобусов уникальны (в случайной коллекции они могут повторяться,
        // но проверим что формат корректен)
        result.stream()
                .map(Bus::getBusNumber)
                .forEach(number -> assertTrue(number.matches("AB\\d{3}"),
                        "Номер должен соответствовать формату ABXXX"));
    }

    @Test
    @DisplayName("Парсинг валидной строки в автобус")
    void testCreateBusFromString_ValidString() {
        // Arrange
        String validData = "A123,Mercedes,50000";

        // Act - используем рефлексию для вызова приватного метода
        Bus result = invokePrivateCreateBusFromString(validData);

        // Assert
        assertNotNull(result, "Должен быть создан автобус из валидной строки");
        assertEquals("A123", result.getBusNumber());
        assertEquals("Mercedes", result.getModel());
        assertEquals(50000, result.getMileage());
    }

    @Test
    @DisplayName("Парсинг невалидной строки в автобус")
    void testCreateBusFromString_InvalidString() {
        // Arrange
        String[] invalidData = {
                "OnlyTwoFields,BMW",           // недостаточно полей
                "A123,Mercedes,NotANumber",    // пробег не число
                "",                            // пустая строка
                "   "                          // строка с пробелами
        };

        for (String data : invalidData) {
            // Act
            Bus result = invokePrivateCreateBusFromString(data);

            // Assert
            assertNull(result, "Должен вернуть null для невалидной строки: " + data);
        }
    }

    @Test
    @DisplayName("Парсинг строки с отрицательным пробегом")
    void testCreateBusFromString_NegativeMileage() {
        // Arrange
        String dataWithNegativeMileage = "A123,Mercedes,-100";

        // Act
        Bus result = invokePrivateCreateBusFromString(dataWithNegativeMileage);

        // Assert
        assertNull(result, "Должен вернуть null для отрицательного пробега");
    }

    @Test
    @DisplayName("Парсинг строки с пробелами")
    void testCreateBusFromString_WithSpaces() {
        // Arrange
        String dataWithSpaces = "  A123  ,  Mercedes  ,  50000  ";

        // Act
        Bus result = invokePrivateCreateBusFromString(dataWithSpaces);

        // Assert
        assertNotNull(result, "Должен обрабатывать строки с пробелами");
        assertEquals("A123", result.getBusNumber());
        assertEquals("Mercedes", result.getModel());
        assertEquals(50000, result.getMileage());
    }

    // Вспомогательный метод для вызова приватного метода createBusFromString
    private Bus invokePrivateCreateBusFromString(String data) {
        try {
            var method = InputService.class.getDeclaredMethod("createBusFromString", String.class);
            method.setAccessible(true);
            return (Bus) method.invoke(inputService, data);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка вызова приватного метода", e);
        }
    }

    @Test
    @DisplayName("Интеграционный тест: файл -> коллекция -> проверка целостности")
    void testCreateCollectionFromFile_Integration() throws IOException {
        // Arrange - создаем файл с разнообразными данными
        List<String> fileContent = List.of(
                "BUS001,Mercedes,150000",
                "BUS002,Volvo,80000",
                "BUS003,MAN,200000"
        );
        createTestFileWithContent(fileContent);

        // Act
        BusCollection result = inputService.createCollectionFromFile(testFilename);

        // Assert
        assertEquals(3, result.size());

        // Проверяем что данные корректно сохранены
        Bus bus1 = result.get(0);
        assertEquals("BUS001", bus1.getBusNumber());
        assertEquals("Mercedes", bus1.getModel());
        assertEquals(150000, bus1.getMileage());

        Bus bus2 = result.get(1);
        assertEquals("BUS002", bus2.getBusNumber());
        assertEquals("Volvo", bus2.getModel());
        assertEquals(80000, bus2.getMileage());

        Bus bus3 = result.get(2);
        assertEquals("BUS003", bus3.getBusNumber());
        assertEquals("MAN", bus3.getModel());
        assertEquals(200000, bus3.getMileage());
    }
}
