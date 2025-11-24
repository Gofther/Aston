package org.example.sortingApplication;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.domain.BusCollection;
import org.example.sortingApplication.service.ValidationService;

public class BusTest {
    public void testBusBuilder() {
        Bus bus = new Bus.Builder()
                .setBusNumber("AB123")
                .setModel("Mercedes")
                .setMileage(100000)
                .build();

        assert bus.getBusNumber().equals("AB123");
        assert bus.getModel().equals("Mercedes");
        assert bus.getMileage() == 100000;
    }

    public void testBusValidation() {
        ValidationService validator = new ValidationService();
        assert validator.validateBusNumber("AB123") == true;
        assert validator.validateBusNumber("") == false;
        assert validator.validateMileage(500000) == true;
        assert validator.validateMileage(-100) == false;
    }

    public void testBusCollection() {
        BusCollection collection = new BusCollection();
        Bus bus = new Bus.Builder()
                .setBusNumber("TEST")
                .setModel("TEST")
                .setMileage(0)
                .build();

        collection.add(bus);
        assert collection.size() == 1;
        assert collection.get(0).equals(bus);
    }
}