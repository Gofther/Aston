package org.example.sortingApplication.service;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.domain.BusCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public void writeToFile(String filename, BusCollection collection, boolean append) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, append))) {
            for (Bus bus : collection) {
                writer.println(bus.getBusNumber() + "," + bus.getModel() + "," + bus.getMileage());
            }
            System.out.println("Данные записаны в файл: " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    public List<String> readFromFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
        return lines;
    }
}