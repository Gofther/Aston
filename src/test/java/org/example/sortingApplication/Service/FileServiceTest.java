package org.example.sortingApplication.Service;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.domain.BusCollection;
import org.example.sortingApplication.service.FileService;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {
    private FileService fileService;
    private String testFilename;
    private BusCollection testCollection;

    @BeforeEach
    void setUp() {
        fileService = new FileService();
        testFilename = "test_buses.txt";
        testCollection = createTestBusCollection();

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

    private BusCollection createTestBusCollection() {
        BusCollection collection = new BusCollection();
        collection.add(new Bus.Builder()
                .setBusNumber("A123")
                .setModel("Mercedes")
                .setMileage(50000)
                .build());
        collection.add(new Bus.Builder()
                .setBusNumber("B456")
                .setModel("Volvo")
                .setMileage(30000)
                .build());
        collection.add(new Bus.Builder()
                .setBusNumber("C789")
                .setModel("BMW")
                .setMileage(40000)
                .build());
        return collection;
    }

    @Test
    @DisplayName("Запись коллекции в файл в режиме перезаписи")
    void testWriteToFile_OverwriteMode() {
        fileService.writeToFile(testFilename, testCollection, false);

        assertTrue(Files.exists(Paths.get(testFilename)), "Файл должен быть создан");

        List<String> lines = fileService.readFromFile(testFilename);
        assertEquals(3, lines.size(), "Должно быть 3 строки в файле");
        assertEquals("A123,Mercedes,50000", lines.get(0));
        assertEquals("B456,Volvo,30000", lines.get(1));
        assertEquals("C789,BMW,40000", lines.get(2));
    }

    @Test
    @DisplayName("Запись коллекции в файл в режиме добавления")
    void testWriteToFile_AppendMode() {
        // Arrange - сначала записываем некоторые данные
        fileService.writeToFile(testFilename, testCollection, false);

        // Act - добавляем ещё данные
        BusCollection additionalCollection = new BusCollection();
        additionalCollection.add(new Bus.Builder()
                .setBusNumber("D999")
                .setModel("Audi")
                .setMileage(25000)
                .build());

        fileService.writeToFile(testFilename, additionalCollection, true);

        // Assert
        List<String> lines = fileService.readFromFile(testFilename);
        assertEquals(4, lines.size(), "Должно быть 4 строки после добавления");
        assertEquals("D999,Audi,25000", lines.get(3));
    }

    @Test
    @DisplayName("Чтение из существующего файла")
    void testReadFromFile_ExistingFile() {
        // Arrange - создаём файл с данными
        fileService.writeToFile(testFilename, testCollection, false);

        // Act
        List<String> lines = fileService.readFromFile(testFilename);

        // Assert
        assertNotNull(lines, "Список не должен быть null");
        assertEquals(3, lines.size(), "Должно быть прочитано 3 строки");
        assertTrue(lines.contains("A123,Mercedes,50000"));
        assertTrue(lines.contains("B456,Volvo,30000"));
        assertTrue(lines.contains("C789,BMW,40000"));
    }

    @Test
    @DisplayName("Чтение из несуществующего файла")
    void testReadFromFile_NonExistentFile() {
        // Act
        List<String> lines = fileService.readFromFile("non_existent_file.txt");

        // Assert
        assertNotNull(lines, "Список не должен быть null");
        assertTrue(lines.isEmpty(), "Список должен быть пустым для несуществующего файла");
    }

    @Test
    @DisplayName("Чтение пустого файла")
    void testReadFromFile_EmptyFile() throws IOException {
        // Arrange - создаём пустой файл
        Files.createFile(Paths.get(testFilename));

        // Act
        List<String> lines = fileService.readFromFile(testFilename);

        // Assert
        assertNotNull(lines);
        assertTrue(lines.isEmpty(), "Список должен быть пустым для пустого файла");
    }

    @Test
    @DisplayName("Чтение файла с пустыми строками")
    void testReadFromFile_FileWithEmptyLines() throws IOException {
        // Arrange - создаём файл с пустыми строками
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilename))) {
            writer.println("A123,Mercedes,50000");
            writer.println();  // пустая строка
            writer.println("B456,Volvo,30000");
            writer.println("   ");  // строка с пробелами
            writer.println("C789,BMW,40000");
        }

        // Act
        List<String> lines = fileService.readFromFile(testFilename);

        // Assert
        assertEquals(3, lines.size(), "Пустые строки должны быть отфильтрованы");
        assertFalse(lines.contains(""), "Не должно быть пустых строк");
        assertFalse(lines.contains("   "), "Не должно быть строк с пробелами");
    }

    @Test
    @DisplayName("Запись пустой коллекции")
    void testWriteToFile_EmptyCollection() {
        // Arrange
        BusCollection emptyCollection = new BusCollection();

        // Act
        fileService.writeToFile(testFilename, emptyCollection, false);

        // Assert
        List<String> lines = fileService.readFromFile(testFilename);
        assertTrue(lines.isEmpty(), "Файл должен быть пустым при записи пустой коллекции");
    }

    @Test
    @DisplayName("Запись в файл с специальными символами в данных")
    void testWriteToFile_WithSpecialCharacters() {
        // Arrange
        BusCollection specialCollection = new BusCollection();
        specialCollection.add(new Bus.Builder()
                .setBusNumber("A-100/1")
                .setModel("Mercedes-Benz")
                .setMileage(50000)
                .build());

        // Act
        fileService.writeToFile(testFilename, specialCollection, false);

        // Assert
        List<String> lines = fileService.readFromFile(testFilename);
        assertEquals(1, lines.size());
        assertEquals("A-100/1,Mercedes-Benz,50000", lines.get(0));
    }

    @Test
    @DisplayName("Многократная запись в режиме добавления")
    void testWriteToFile_MultipleAppends() {
        // Act - несколько записей в режиме добавления
        BusCollection collection1 = new BusCollection();
        collection1.add(new Bus.Builder().setBusNumber("A111").setModel("Model1").setMileage(10000).build());
        fileService.writeToFile(testFilename, collection1, false);

        BusCollection collection2 = new BusCollection();
        collection2.add(new Bus.Builder().setBusNumber("B222").setModel("Model2").setMileage(20000).build());
        fileService.writeToFile(testFilename, collection2, true);

        BusCollection collection3 = new BusCollection();
        collection3.add(new Bus.Builder().setBusNumber("C333").setModel("Model3").setMileage(30000).build());
        fileService.writeToFile(testFilename, collection3, true);

        // Assert
        List<String> lines = fileService.readFromFile(testFilename);
        assertEquals(3, lines.size(), "Должно быть 3 строки после многократного добавления");
        assertEquals("A111,Model1,10000", lines.get(0));
        assertEquals("B222,Model2,20000", lines.get(1));
        assertEquals("C333,Model3,30000", lines.get(2));
    }

    @Test
    @DisplayName("Поведение при ошибках ввода-вывода")
    void testWriteToFile_IOExceptionHandling() {
        // Arrange - попытка записи в защищённую директорию
        String protectedFilename = "/root/protected/test.txt";

        // Act & Assert - не должно быть исключения, должно быть сообщение об ошибке
        assertDoesNotThrow(() -> {
            BusCollection collection = new BusCollection();
            collection.add(new Bus.Builder().setBusNumber("A123").setModel("Test").setMileage(1000).build());
            fileService.writeToFile(protectedFilename, collection, false);
        }, "Метод должен обрабатывать IOException без выбрасывания исключения");
    }
}