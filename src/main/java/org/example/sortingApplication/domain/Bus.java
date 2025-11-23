package org.example.sortingApplication.domain;

import java.util.Objects;

public class Bus {
    private final String busNumber;     // Номер автобуса
    private final String model;         // Модель
    private final int mileage;          // Пробег

    private Bus(Builder builder) {
        this.busNumber = builder.busNumber;
        this.model = builder.model;
        this.mileage = builder.mileage;
    }

    // Геттеры
    public String getBusNumber() { return busNumber; }
    public String getModel() { return model; }
    public int getMileage() { return mileage; }

    // Builder Pattern
    public static class Builder {
        private String busNumber;
        private String model;
        private int mileage;

        public Builder setBusNumber(String busNumber) {
            this.busNumber = busNumber;
            return this;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public Builder setMileage(int mileage) {
            this.mileage = mileage;
            return this;
        }

        public Bus build() {
            return new Bus(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Автобус №%s, Модель: %s, Пробег: %d км",
                busNumber, model, mileage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Bus)) return false;
        Bus other = (Bus) obj;
        return Objects.equals(busNumber, other.busNumber) &&
               Objects.equals(model, other.model) &&
               mileage == other.mileage;
    }
}