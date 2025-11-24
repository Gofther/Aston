package org.example.sortingApplication.util;

import java.util.Scanner;

public class InitComporator {

    public BusComparator init(Scanner scanner) {
        System.out.println("Выберите в каком порядке сортировать автобусы, введите числа от 1 до 3 в нужном вам порядке через пробел," +
                " которые соответствуют полям автобуса:\n1. Номер\n2. Модель\n3. Пробег");

        scanner.nextLine();
        String choice = scanner.nextLine().trim();

        if (choice.isEmpty()) {
            throw new IllegalArgumentException("Не введен порядок сортировки");
        }

        String[] arrayChoice = choice.split("\\s+");

        for (String num : arrayChoice) {
            if (!num.matches("[1-3]")) {
                throw new IllegalArgumentException("Можно использовать только числа 1, 2, 3");
            }
        }
        String[] compareOrder = new String[3];
        for (int i = 0; i < arrayChoice.length; i++) {
            if (arrayChoice[i].equals("1")) {
                compareOrder[i] = "номер";
            } else if (arrayChoice[i].equals("2")) {
                compareOrder[i] = "модель";
            } else {
                compareOrder[i] = "пробег";
            }
        }

        return new BusComparator(compareOrder[0], compareOrder[1], compareOrder[2]);
    }
}
