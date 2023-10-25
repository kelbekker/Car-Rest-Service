package com.foxminded.car.repository;

import com.foxminded.car.model.Car;
import com.foxminded.car.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE c.make = :make")
    List<Car> getCarListByManufacturer(@Param("make") String make);

    @Query("SELECT c FROM Car c WHERE c.model = :model")
    List<Car> getCarListByModel(@Param("model") String model);

    @Query("SELECT c FROM Car c JOIN c.category cat WHERE cat IN :categories")
    List<Car> getCarListByCategories(@Param("categories") List<Category> categories);

    @Query("SELECT c FROM Car c WHERE c.year BETWEEN :minYear AND :maxYear")
    List<Car> getCarListByYearRange(@Param("minYear") int minYear,@Param("maxYear") int maxYear);
}
