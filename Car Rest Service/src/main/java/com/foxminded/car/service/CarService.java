package com.foxminded.car.service;

import com.foxminded.car.model.Car;
import com.foxminded.car.model.Category;
import com.foxminded.car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarService {

    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void importDataFromCsv() {
        try (InputStream inputStream = getClass().getResourceAsStream("/file.csv");
             InputStreamReader streamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip the header line
                }
                Car car = new Car();

                if (line.contains("\"")) {
                    String[] fields = line.split("\"");

                    String[] filedsOne = fields[0].split(",");
                    String[] filedsTwo = fields[1].split(",");


                    car.setObjectId(filedsOne[0]);
                    car.setMake(filedsOne[1]);

                    try {
                        String yearValue = filedsOne[2].trim();
                        car.setYear(Integer.parseInt(yearValue));
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing 'year' field: " + e.getMessage());
                        continue;
                    }

                    car.setModel(filedsOne[3]);

                    for (int i = 0; i < filedsTwo.length; i++) {
                        filedsTwo[i] = filedsTwo[i].trim().replace("\"", "");
                    }

                    List<Category> categories = Arrays.stream(filedsTwo)
                            .map(String::trim) // Trim each value
                            .map(this::getCategoryFromString)
                            .collect(Collectors.toList());

                    car.setCategory(categories);

                    carRepository.save(car);
                } else {
                    String[] carLine = line.split(",");

                    car.setObjectId(carLine[0]);
                    car.setMake(carLine[1]);

                    try {
                        String yearValue = carLine[2].trim();
                        car.setYear(Integer.parseInt(yearValue));
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing 'year' field: " + e.getMessage());
                        continue;
                    }

                    car.setModel(carLine[3]);

                    String[] categoryValues = carLine[4].split(",");

                    List<Category> categories = new ArrayList<>();

                    for (String categoryValue : categoryValues) {
                        String trimmedValue = categoryValue.trim();
                        if (trimmedValue.contains("Van/Minivan")) {
                            trimmedValue = trimmedValue.replace("Van/Minivan", "VAN_OR_MINIVAN");
                        }
                        Category category = getCategoryFromString(trimmedValue);
                        categories.add(category);
                    }

                    car.setCategory(categories);
                }
                carRepository.save(car);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Category getCategoryFromString(String categoryValue) {
        try {
            return Category.valueOf(categoryValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No enum constant for category value: " + categoryValue);
        }
    }

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public Car updateCar(Car car) {
        return carRepository.save(car);
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public List<Car> getCarList() {
        return carRepository.findAll();
    }

    public void deleteCarById(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> getListCarByManufacturer(String make) {
        return carRepository.getCarListByManufacturer(make);
    }

    public List<Car> getListCarByModel(String model) {
        return carRepository.getCarListByModel(model);
    }

    public List<Car> getListCarByCategory(String categoryStrings) {
        List<String> categoryStringList = Arrays.asList(categoryStrings.split(","));

        List<Category> categories = categoryStringList.stream()
                .map(categoryString -> Category.valueOf(categoryString.toUpperCase()))
                .collect(Collectors.toList());
        return carRepository.getCarListByCategories(categories);
    }

    public List<Car> getListCarByYearRange(int minYear, int maxYear) {
        return carRepository.getCarListByYearRange(minYear, maxYear);
    }
}