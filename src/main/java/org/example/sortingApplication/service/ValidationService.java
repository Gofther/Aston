package org.example.sortingApplication.service;

public class ValidationService {
    public boolean validateBusData(String busNumber, String model, int mileage) {
        return validateBusNumber(busNumber) &&
               validateModel(model) &&
               validateMileage(mileage);
    }

    public boolean validateBusNumber(String busNumber) {
        return busNumber != null && !busNumber.trim().isEmpty() && busNumber.length() <= 10;
    }

    public boolean validateModel(String model) {
        return model != null && !model.trim().isEmpty() && model.length() <= 50;
    }

    public boolean validateMileage(int mileage) {
        return mileage >= 0 && mileage <= 1000000;
    }

    public boolean validateInputData(String[] data) {
        return data != null && data.length == 3;
    }
}