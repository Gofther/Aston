package org.example.sortingApplication.strategy;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.domain.BusCollection;

import java.util.Comparator;

public class TimSortStrategy implements SortStrategy {
    private static final int MIN_MERGE = 32;
    private final Comparator<Bus> comparator;

    public TimSortStrategy(Comparator<Bus> comparator) {
        this.comparator = comparator;
    }

    /**
     * Сортирует массив автобусов using TimSort algorithm
     */
    @Override
    public Bus[] sort(Bus[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }

        int n = array.length;

        // 1. Сортируем маленькие подмассивы сортировкой вставками
        for (int i = 0; i < n; i += MIN_MERGE) {
            int end = Math.min(i + MIN_MERGE - 1, n - 1);
            insertionSort(array, i, end);
        }

        // 2. Сливаем подмассивы возрастающего размера
        for (int size = MIN_MERGE; size < n; size = 2 * size) {
            for (int left = 0; left < n; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min(left + 2 * size - 1, n - 1);

                // Сливаем только если есть что сливать
                if (mid < right) {
                    merge(array, left, mid, right);
                }
            }
        }
        return array;
    }

    /**
     * Сортировка вставками для маленьких подмассивов
     */
    private void insertionSort(Bus[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            Bus current = array[i];
            int j = i - 1;

            // Сдвигаем элементы больше текущего вправо
            while (j >= left && comparator.compare(array[j], current) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = current;
        }
    }

    /**
     * Слияние двух отсортированных подмассивов
     * array[left...mid] и array[mid+1...right]
     */
    public void merge(Bus[] array, int left, int mid, int right) {
        // Длины подмассивов
        int len1 = mid - left + 1;
        int len2 = right - mid;

        // Создаем временные массивы
        Bus[] leftArray = new Bus[len1];
        Bus[] rightArray = new Bus[len2];

        // Копируем данные во временные массивы
        System.arraycopy(array, left, leftArray, 0, len1);
        System.arraycopy(array, mid + 1, rightArray, 0, len2);

        int i = 0, j = 0; // Индексы для временных массивов
        int k = left;     // Индекс для основного массива

        // Сливаем временные массивы обратно в array[left...right]
        while (i < len1 && j < len2) {
            if (comparator.compare(leftArray[i], rightArray[j]) <= 0) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы leftArray
        while (i < len1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы rightArray
        while (j < len2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }

    @Override
    public String getDescription() {
        return "TimSort Algorithm (гибридная сортировка слиянием и вставками)";
    }
}
