package org.example.sortingApplication.service;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.strategy.SortStrategy;

public class SortingService {
    private SortStrategy strategy;

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;  // ✅ Устанавливаем стратегию
    }

    public Bus[] performSort(Bus[] buses) {
        if (strategy == null) {
            throw new IllegalStateException("Стратегия не установлена");
        }
        return strategy.sort(buses);  // ✅ Вызываем алгоритм через интерфейс
    }
}
