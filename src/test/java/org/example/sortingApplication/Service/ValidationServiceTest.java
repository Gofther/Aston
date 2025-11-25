package org.example.sortingApplication.Service;

import org.example.sortingApplication.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
    }

    // Тесты для validateBusNumber
    @Test
    void testValidateBusNumber_Valid() {
        assertTrue(validationService.validateBusNumber("ABC123"));
        assertTrue(validationService.validateBusNumber("A1"));
        assertTrue(validationService.validateBusNumber("1234567890")); // максимальная длина
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void testValidateBusNumber_InvalidNullOrEmpty(String busNumber) {
        assertFalse(validationService.validateBusNumber(busNumber));
    }

    @Test
    void testValidateBusNumber_TooLong() {
        assertFalse(validationService.validateBusNumber("12345678901")); // 11 символов
    }

    // Тесты для validateModel
    @Test
    void testValidateModel_Valid() {
        assertTrue(validationService.validateModel("Mercedes Sprinter"));
        assertTrue(validationService.validateModel("A"));
        assertTrue(validationService.validateModel("M".repeat(50))); // максимальная длина
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void testValidateModel_InvalidNullOrEmpty(String model) {
        assertFalse(validationService.validateModel(model));
    }

    @Test
    void testValidateModel_TooLong() {
        assertFalse(validationService.validateModel("M".repeat(51))); // 51 символ
    }

    // Тесты для validateMileage
    @Test
    void testValidateMileage_Valid() {
        assertTrue(validationService.validateMileage(0));
        assertTrue(validationService.validateMileage(50000));
        assertTrue(validationService.validateMileage(1000000)); // максимальное значение
    }

    @Test
    void testValidateMileage_Negative() {
        assertFalse(validationService.validateMileage(-1));
        assertFalse(validationService.validateMileage(-1000));
    }

    @Test
    void testValidateMileage_TooHigh() {
        assertFalse(validationService.validateMileage(1000001));
        assertFalse(validationService.validateMileage(Integer.MAX_VALUE));
    }

    // Тесты для validateBusData (комплексная валидация)
    @Test
    void testValidateBusData_AllValid() {
        assertTrue(validationService.validateBusData("ABC123", "Mercedes", 50000));
    }

    @Test
    void testValidateBusData_InvalidBusNumber() {
        assertFalse(validationService.validateBusData(null, "Mercedes", 50000));
        assertFalse(validationService.validateBusData("", "Mercedes", 50000));
        assertFalse(validationService.validateBusData("12345678901", "Mercedes", 50000));
    }

    @Test
    void testValidateBusData_InvalidModel() {
        assertFalse(validationService.validateBusData("ABC123", null, 50000));
        assertFalse(validationService.validateBusData("ABC123", "", 50000));
        assertFalse(validationService.validateBusData("ABC123", "M".repeat(51), 50000));
    }

    @Test
    void testValidateBusData_InvalidMileage() {
        assertFalse(validationService.validateBusData("ABC123", "Mercedes", -1));
        assertFalse(validationService.validateBusData("ABC123", "Mercedes", 1000001));
    }

    // Тесты для validateInputData
    @Test
    void testValidateInputData_Valid() {
        String[] validData = {"ABC123", "Mercedes", "50000"};
        assertTrue(validationService.validateInputData(validData));
    }

    @Test
    void testValidateInputData_Null() {
        assertFalse(validationService.validateInputData(null));
    }

    @Test
    void testValidateInputData_WrongLength() {
        String[] shortData = {"ABC123", "Mercedes"}; // 2 элемента
        String[] longData = {"ABC123", "Mercedes", "50000", "Extra"}; // 4 элемента
        String[] emptyData = {};

        assertFalse(validationService.validateInputData(shortData));
        assertFalse(validationService.validateInputData(longData));
        assertFalse(validationService.validateInputData(emptyData));
    }

    // Параметризованные тесты для граничных значений
    @ParameterizedTest
    @CsvSource({
            "ABC123, Mercedes, 0, true",        // минимальный пробег
            "ABC123, Mercedes, 1000000, true",  // максимальный пробег
            "1234567890, M, 500000, true",      // максимальная длина номер + минимальная длина модели
            "12345678901, Mercedes, 50000, false", // слишком длинный номер
            "ABC123, Mercedes, -1, false",      // отрицательный пробег
            "ABC123, Mercedes, 1000001, false"  // слишком большой пробег
    })
    void testValidateBusData_BoundaryValues(String busNumber, String model, int mileage, boolean expected) {
        assertEquals(expected, validationService.validateBusData(busNumber, model, mileage));
    }

    // Тест на производительность (опционально)
    @Test
    void testValidateBusData_Performance() {
        long startTime = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            validationService.validateBusData("ABC123", "Mercedes", 50000);
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        // Проверяем что выполняется достаточно быстро (менее 1 секунды для 1000 вызовов)
        assertTrue(duration < 1_000_000_000, "Validation should be fast");
    }
}
