package com.foxminded.car.repository;

import com.foxminded.car.model.Car;
import com.foxminded.car.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        CarRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    public void setup() {
        Car car1 = new Car("ZRgPP9dBMm","Audi",2020,"Q3", List.of(Category.SUV));
        carRepository.save(car1);
        Car car2 = new Car("cptB1C1NSL","Chevrolet",2020,"Malibu", List.of(Category.SEDAN));
        carRepository.save(car2);
    }

    @Test
    public void getCarListByManufacturerWhenNormalMakeShouldReturnCarList() {
        List<Car> carList = carRepository.getCarListByManufacturer("Chevrolet");

        assertEquals(1, carList.size());
        assertEquals("Chevrolet", carList.get(0).getMake());
        assertEquals(List.of(Category.SEDAN), carList.get(0).getCategory());
    }

    @Test
    public void getCarListByModelWhenNormalModelShouldReturnCarList() {
        List<Car> carList = carRepository.getCarListByModel("Malibu");

        assertEquals(1, carList.size());
        assertEquals("Malibu", carList.get(0).getModel());
        assertEquals(2020, carList.get(0).getYear());
    }

    @Test
    public void getCarListByCategoriesWhenNormalCategoriesShouldReturnCarList() {
        List<Car> carList = carRepository.getCarListByCategories(List.of(Category.SUV));

        assertEquals(1, carList.size());
        assertEquals("Q3", carList.get(0).getModel());
        assertEquals("ZRgPP9dBMm", carList.get(0).getObjectId());
    }

    @Test
    public void getCarListByYearRangeWhenNormalYearsShouldReturnCarList() {
        List<Car> carList = carRepository.getCarListByYearRange(2010, 2023);

        assertEquals(2, carList.size());
        assertEquals("Q3", carList.get(0).getModel());
        assertEquals("Malibu", carList.get(1).getModel());
        assertEquals("ZRgPP9dBMm", carList.get(0).getObjectId());
        assertEquals("cptB1C1NSL", carList.get(1).getObjectId());
    }
}
