package org.example.sortingApplication.util;

import org.example.sortingApplication.domain.Bus;

import java.util.Comparator;
import java.util.Scanner;

public class BusComparator implements Comparator<Bus> {
    private String primaryField;
    private String secondaryField;
    private String tertiaryField;
    private boolean caseSensitive; // учитывать ли регистр

    public BusComparator(String primaryField, String secondaryField, String tertiaryField) {
        this(primaryField, secondaryField, tertiaryField, true);
    }

    public BusComparator(String primaryField, String secondaryField, String tertiaryField, boolean caseSensitive) {
        this.primaryField = primaryField;
        this.secondaryField = secondaryField;
        this.tertiaryField = tertiaryField;
        this.caseSensitive = caseSensitive;
    }

    @Override
    public int compare(Bus bus1, Bus bus2) {
        int primaryComparison = compareByField(bus1, bus2, primaryField);
        if (primaryComparison != 0) return primaryComparison;

        int secondaryComparison = compareByField(bus1, bus2, secondaryField);
        if (secondaryComparison != 0) return secondaryComparison;

        return compareByField(bus1, bus2, tertiaryField);
    }

    private int compareByField(Bus bus1, Bus bus2, String field) {
        switch (field.toLowerCase()) {
            case "номер":
                return compareStrings(bus1.getBusNumber(), bus2.getBusNumber());
            case "модель":
                return compareStrings(bus1.getModel(), bus2.getModel());
            case "пробег":
                return Integer.compare(bus1.getMileage(), bus2.getMileage());
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }
    }

    private int compareStrings(String str1, String str2) {
        if (caseSensitive) {
            return str1.compareTo(str2); // с учетом регистра
        } else {
            return str1.compareToIgnoreCase(str2); // без учета регистра
        }
    }

}
