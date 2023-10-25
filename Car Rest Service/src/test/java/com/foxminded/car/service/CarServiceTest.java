package com.foxminded.car.service;

import com.foxminded.car.model.Car;
import com.foxminded.car.model.Category;
import com.foxminded.car.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        CarService.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Car car1 = new Car("ZRgPP9dBMm","Audi",2020,"Q3", List.of(Category.SUV));
        carService.createCar(car1);
        Car car2 = new Car("cptB1C1NSL","Chevrolet",2020,"Malibu", List.of(Category.SEDAN));
        carService.createCar(car2);
    }

    @Test
    public void importDataFromCsvWhenNormalShouldImportData() {
        carService.importDataFromCsv();

        verify(carRepository, times(7)).save(any(Car.class));
    }

    @Test
    public void createCarWhenNormalCarShouldReturnCar() {
        Car car = carService.getCarById(1L);
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car createdCar = carService.createCar(car);

        verify(carRepository, times(1)).save(car);

        assertEquals(car, createdCar);
    }

    @Test
    public void updateCarWhenNormalCarShouldReturnCar() {
        Car car = carService.getCarById(1L);
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car createdCar = carService.updateCar(car);

        verify(carRepository, times(1)).save(car);

        assertEquals(car, createdCar);
    }

    @Test
    public void getByIdWhenNormalCarIdShouldReturnCar() {
        long carId = 1L;
        Car car = new Car();
        car.setId(carId);
        car.setMake("Toyota");

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Car retrievedCar = carService.getCarById(carId);

        verify(carRepository, times(1)).findById(carId);

        assertNotNull(retrievedCar);

        assertEquals(car, retrievedCar);
    }

    @Test
    public void getCarListWhenNormalShouldReturnCarList() {
        List<Car> sampleCarList = Arrays.asList(carService.getCarById(1L), carService.getCarById(2L));

        when(carRepository.findAll()).thenReturn(sampleCarList);

        List<Car> retrievedCarList = carService.getCarList();

        verify(carRepository, times(1)).findAll();

        assertNotNull(retrievedCarList);
        assertEquals(sampleCarList, retrievedCarList);
    }

    @Test
    public void deleteCarByIdWhenNormalCarIdShouldDeleteCar() {
        carService.deleteCarById(1L);

        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    public void getListCarByManufacturerWhenNormalManufacturerShouldReturnCarList() {
        String manufacturer = "Chevrolet";
        List<Car> sampleCarList = Arrays.asList(carService.getCarById(2L));

        when(carRepository.getCarListByManufacturer(manufacturer)).thenReturn(sampleCarList);

        List<Car> retrievedCarList = carService.getListCarByManufacturer(manufacturer);

        verify(carRepository, times(1)).getCarListByManufacturer(manufacturer);

        assertNotNull(retrievedCarList);
        assertEquals(sampleCarList, retrievedCarList);
    }

    @Test
    public void getListCarByModelWhenNormalModelShouldReturnCarList() {
        String model = "Q3";
        List<Car> sampleCarList = Arrays.asList(carService.getCarById(1L));

        when(carRepository.getCarListByModel(model)).thenReturn(sampleCarList);

        List<Car> retrievedCarList = carService.getListCarByModel(model);

        verify(carRepository, times(1)).getCarListByModel(model);

        assertNotNull(retrievedCarList);
        assertEquals(sampleCarList, retrievedCarList);
    }

    @Test
    public void getListCarByCategoryWhenNormalCategoryShouldReturnCarList() {
        String categoryStrings = "SEDAN,SUV";

        List<String> categoryStringList = Arrays.asList(categoryStrings.split(","));
        List<Category> categories = categoryStringList.stream()
                .map(categoryString -> Category.valueOf(categoryString.trim().toUpperCase()))
                .collect(Collectors.toList());


        List<Car> sampleCarList = Arrays.asList(carService.getCarById(1L), carService.getCarById(2L));

        when(carRepository.getCarListByCategories(categories)).thenReturn(sampleCarList);

        List<Car> retrievedCarList = carService.getListCarByCategory(categoryStrings);

        verify(carRepository, times(1)).getCarListByCategories(categories);

        assertNotNull(retrievedCarList);
        assertEquals(sampleCarList, retrievedCarList);
    }

    @Test
    public void getListCarByYearRangeWhenNormalYearsShouldReturnCarList() {
        int minYear = 2015;
        int maxYear = 2023;
        List<Car> sampleCarList = Arrays.asList(carService.getCarById(1L), carService.getCarById(2L));

        when(carRepository.getCarListByYearRange(minYear, maxYear)).thenReturn(sampleCarList);

       List<Car> retrievedCarList = carService.getListCarByYearRange(minYear, maxYear);

        verify(carRepository, times(1)).getCarListByYearRange(minYear, maxYear);

        assertNotNull(retrievedCarList);
        assertEquals(sampleCarList, retrievedCarList);
    }
}
