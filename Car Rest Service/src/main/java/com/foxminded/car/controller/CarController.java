package com.foxminded.car.controller;

import com.foxminded.car.model.Car;
import com.foxminded.car.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Operation(summary = "Create a car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the car",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid param supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content) })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Car createCar(@RequestBody Car car) {
        return carService.createCar(car);
    }

    @Operation(summary = "Get a car by its id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the car",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content) })
    @GetMapping(value = "/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @Operation(summary = "Update a car by its id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the car",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid car supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content) })
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Car updateCar(@PathVariable Long id, Car car) {
        car = carService.getCarById(id);
        return carService.updateCar(car);
    }

    @Operation(summary = "Delete a car by its id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete the car",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content) })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCarById(id);
    }

    @Operation(summary = "Get cars by its make, model, category, minYear or maxYear", security = @SecurityRequirement
            (name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the cars",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid param supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cars not found",
                    content = @Content) })
    @GetMapping
    public List<Car> getCarsBy(
            @RequestParam(name = "make", required = false) String make,
            @RequestParam(name = "model", required = false) String model,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minYear", required = false) Integer minYear,
            @RequestParam(name = "maxYear", required = false) Integer maxYear
    ) {
        List<Car> filteredCars = carService.getCarList();

        if (make != null) {
            filteredCars = carService.getListCarByManufacturer(make);
        }

        if (model != null) {
            List<Car> modelFilteredCars = carService.getListCarByModel(model);
            filteredCars.retainAll(modelFilteredCars);
        }

        if (category != null) {
            List<Car> categoryFilteredCars = carService.getListCarByCategory(category);
            filteredCars.retainAll(categoryFilteredCars);
        }

        if (minYear != null && maxYear != null) {
            List<Car> yearRangeFilteredCars = carService.getListCarByYearRange(minYear, maxYear);
            filteredCars.retainAll(yearRangeFilteredCars);
        }

        return filteredCars;
    }
}
