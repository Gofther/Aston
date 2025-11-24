package org.example.sortingApplication.strategy;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.domain.BusCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EvenTimSortStrategy implements SortStrategy {
    private final Comparator<Bus> comparator;

    public EvenTimSortStrategy(Comparator<Bus> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Bus[] sort(Bus[] buses) {
        TimSortStrategy timSortStrategy = new TimSortStrategy(comparator);

        // 1. Собираем четные автобусы и их позиции
        BusCollection evens = new BusCollection();
        List<Integer> positions = new ArrayList<>();

        for(int i = 0; i < buses.length; i++) {
            if(buses[i] != null && buses[i].getMileage() % 2 == 0) {
                evens.add(buses[i]);
                positions.add(i);
            }
        }

        // 2. Получаем массив из коллекции и сортируем его
        Bus[] evenArray = evens.toArray();
        timSortStrategy.sort(evenArray);

        // 3. Заменяем четные элементы в исходном массиве
        for(int i = 0; i < evenArray.length; i++) {
            buses[positions.get(i)] = evenArray[i];
        }

        return buses;
    }

    @Override
    public String getDescription() {
        return "TimSort (только автобусы с четным пробегом сортируются)";
    }
}
