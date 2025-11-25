package org.example.sortingApplication.strategy;

import org.example.sortingApplication.domain.Bus;

public interface SortStrategy {
    Bus[] sort(Bus[] buses);
    String getDescription();
}
