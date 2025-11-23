package org.example.sortingApplication;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.domain.BusCollection;
import org.example.sortingApplication.service.FileService;
import org.example.sortingApplication.service.InputService;
import org.w3c.dom.ls.LSOutput;

import java.util.Scanner;

public class Main {

    //Автобус №114, Модель: B15UH, Пробег: 12 км
//Автобус №221, Модель: V22YE, Пробег: 98 км
//Автобус №942, Модель: H62MK, Пробег: 66 км
//Автобус №678, Модель: B54UX, Пробег: 21 км
//Автобус №289, Модель: M10TX, Пробег: 112 км

    private static Scanner scanner = new Scanner(System.in);
    private static InputService inputService = new InputService();
    private static FileService fileService = new FileService();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleManualInput();
                    break;
                case 2:
                    handleRandomInput();
                    break;
                case 3:
                    handleFileInput();
                    break;
                case 4:
                    running = false;
                    System.out.println("Выход из программы");
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n=== Система сортировки автобусов ===");
        System.out.println("1. Ручной ввод данных");
        System.out.println("2. Случайные данные");
        System.out.println("3. Загрузка из файла");
        System.out.println("4. Выход");
        System.out.print("Выберите вариант: ");
    }

    private static int getUserChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // clear invalid input
            return -1;
        }
    }

    private static void handleManualInput() {
        BusCollection collection = inputService.createCollectionManual();
        processCollection(collection);
    }

    private static void handleRandomInput() {
        System.out.print("Введите количество автобусов: ");
        int size = scanner.nextInt();
        BusCollection collection = inputService.createCollectionRandom(size);
        processCollection(collection);
    }

    private static void handleFileInput() {
        System.out.print("Введите имя файла: ");
        scanner.nextLine();
        String filename = scanner.nextLine();
        BusCollection collection = inputService.createCollectionFromFile(filename);
        processCollection(collection);
    }

    private static void processCollection(BusCollection collection) {
        // Здесь будет взаимодействие со стратегиями сортировки
        System.out.println("Коллекция создана, размер: " + collection.size());
        // Временный вывод для демонстрации
         for (Bus bus : collection) {
                    System.out.println(bus);
                }
        System.out.println("Как бы вы хотели сортировать ваш список?");
        System.out.println("1. пузырьком");
        System.out.println("2. Косая");
        System.out.println("3. Березовая");
        System.out.println("4. назад");

        System.out.print("Выберите пункт: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("сортировка пузырьком");
                System.out.println("safsafsa");
                break;
            case "2":
                System.out.println("Косая сортировка");
                System.out.println("xzcscsac");
                break;
            case "3":
                System.out.println("Березовая сортировка");
                System.out.println("xzcscsac");
                break;
            case "4":
                System.out.println("Возвращаемся назад!!!");
                break;
        }


        System.out.println();
        System.out.println("Тут будет сортировка");
    }
}