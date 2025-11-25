package org.example.sortingApplication.util;

import java.util.Scanner;

public class InitComparator {

    public BusComparator init(Scanner scanner) {
        System.out.println("Выберите в каком порядке сортировать автобусы, введите числа от 1 до 3 в нужном вам порядке через пробел," +
                " которые соответствуют полям автобуса:\n1. Номер\n2. Модель\n3. Пробег");
        //если выбираем сортировку только четных, то есть смысл вводить только 3, так в задании написано
        scanner.nextLine();
        String choice = scanner.nextLine().trim();

        if (choice.isEmpty()) {
            System.out.println("Не введен порядок сортировки");
            return null;
        }

        String[] arrayChoice = choice.split("\\s+");

        if (arrayChoice.length > 3) {
            System.out.println("Нельзя вводить больше 3 чисел");
            return null;
        }

        for (String num : arrayChoice) {
            if (!num.matches("[1-3]")) {
                System.out.println("Можно использовать только числа 1, 2, 3");
                return null;
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
