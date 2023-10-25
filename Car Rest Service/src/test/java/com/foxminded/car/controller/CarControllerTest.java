package com.foxminded.car.controller;

import static org.mockito.Mockito.*;
import com.foxminded.car.model.Car;
import com.foxminded.car.model.Category;
import com.foxminded.car.service.CarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(CarController.class)
@ExtendWith(SpringExtension.class)
public class CarControllerTest {

    @MockBean
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCarWhenNormalCarShouldReturnCar() throws Exception {
        Car car1 = new Car("ZRgPP9dBMm","Audi",2020,"Q3", List.of(Category.SUV));

        when(carService.createCar(car1)).thenReturn(car1);

        Car result = carController.createCar(car1);

        Mockito.verify(carService).createCar(car1);

        Assertions.assertEquals(car1, result);
    }

    @Test
    public void getCarByIdWhenNormalCarIdShouldReturnCar() throws Exception {
        Car car1 = new Car(1L, "ZRgPP9dBMm","Audi",2020,"Q3", List.of(Category.SUV));

        when(carService.getCarById(1L)).thenReturn(car1);

        Car result = carController.getCarById(1L);

        Mockito.verify(carService).getCarById(1L);

        Assertions.assertEquals(car1, result);
    }

    @Test
    public void updateCarWhenNormalCarShouldReturnCar() throws Exception {
        Car car1 = new Car(1L, "ZRgPP9dBMm","Audi",2020,"Q3", List.of(Category.SUV));

        when(carService.getCarById(1L)).thenReturn(car1);
        when(carService.updateCar(car1)).thenReturn(car1);

        Car result = carController.updateCar(1L, car1);

        Mockito.verify(carService).updateCar(car1);

        Assertions.assertEquals(car1, result);
    }

    @Test
    public void deleteCarWhenNormalCarIdShouldDeleteCar() throws Exception {
        Car car1 = new Car(1L, "ZRgPP9dBMm","Audi",2020,"Q3", List.of(Category.SUV));

        carController.deleteCar(1L);

        Mockito.verify(carService).deleteCarById(1L);
    }

    @Test
    public void getCarsWhenNormalCarsParamsShouldReturnCarList() throws Exception {
        String make = "Cadillac";
        String model = "Q3";
        String category = "suv";
        Integer minYear = 2019;
        Integer maxYear = 2021;

        Car car1 = new Car("ZRgPP9dBMm","Audi",2020,"Q3", List.of(Category.SUV));
        Car car2 = new Car("cptB1C1NSL","Chevrolet",2020,"Malibu", List.of(Category.SEDAN));
        List<Car> filteredCars = Arrays.asList(car1, car2);

        when(carService.getListCarByManufacturer(make)).thenReturn(filteredCars);
        when(carService.getListCarByModel(model)).thenReturn(filteredCars);
        when(carService.getListCarByCategory(category)).thenReturn(filteredCars);
        when(carService.getListCarByYearRange(minYear, maxYear)).thenReturn(filteredCars);

        List<Car> result = carController.getCarsBy(make, model, category, minYear, maxYear);

        Mockito.verify(carService).getListCarByManufacturer(make);
        Mockito.verify(carService).getListCarByModel(model);
        Mockito.verify(carService).getListCarByCategory(category);
        Mockito.verify(carService).getListCarByYearRange(minYear, maxYear);

        Assertions.assertEquals(filteredCars, result);
    }
}
