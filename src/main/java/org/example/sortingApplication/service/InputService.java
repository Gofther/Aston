package org.example.sortingApplication.service;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.domain.BusCollection;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class InputService {
    private ValidationService validationService;

    public InputService() {
        this.validationService = new ValidationService();
    }

    public BusCollection createCollectionManual() {
        BusCollection collection = new BusCollection();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите количество автобусов:");
        int count = scanner.nextInt();
        scanner.nextLine(); // consume newline

        for (int i = 0; i < count; i++) {
            System.out.println("Автобус " + (i + 1) + ":");
            System.out.print("Номер: ");
            String number = scanner.nextLine();
            System.out.print("Модель: ");
            String model = scanner.nextLine();
            System.out.print("Пробег: ");
            int mileage = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (validationService.validateBusData(number, model, mileage)) {
                Bus bus = new Bus.Builder()
                        .setBusNumber(number)
                        .setModel(model)
                        .setMileage(mileage)
                        .build();
                collection.add(bus);
            } else {
                System.out.println("Неверные данные, пропускаем этот автобус");
                i--; // повторяем ввод для этого автобуса
            }
        }
        return collection;
    }

    public BusCollection createCollectionRandom(int size) {
        BusCollection collection = new BusCollection();
        String[] models = {"Mercedes", "Volvo", "MAN", "Scania", "Iveco"};
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            String number = "AB" + (100 + random.nextInt(900));
            String model = models[random.nextInt(models.length)];
            int mileage = random.nextInt(500000);

            Bus bus = new Bus.Builder()
                    .setBusNumber(number)
                    .setModel(model)
                    .setMileage(mileage)
                    .build();
            collection.add(bus);
        }
        return collection;
    }

    public BusCollection createCollectionFromFile(String filename) {
        BusCollection collection = new BusCollection();
        FileService fileService = new FileService();

        List<String> lines = fileService.readFromFile(filename);
        collection = lines.stream()
                .map(this::createBusFromString)
                .filter(Objects::nonNull)
                .collect(BusCollection::new, BusCollection::add, BusCollection::addAll);

        return collection;
    }

    private Bus createBusFromString(String data) {
        try {
            String[] parts = data.split(",");
            if (parts.length == 3) {
                String number = parts[0].trim();
                String model = parts[1].trim();
                int mileage = Integer.parseInt(parts[2].trim());

                if (validationService.validateBusData(number, model, mileage)) {
                    return new Bus.Builder()
                            .setBusNumber(number)
                            .setModel(model)
                            .setMileage(mileage)
                            .build();
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка парсинга строки: " + data);
        }
        return null;
    }
}