package com.foxminded.car.component;

import com.foxminded.car.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyStartupRunner implements ApplicationRunner {

    @Autowired
    private CarService carService;

    @Value("${app.initialize-db}")
    private boolean initializeDb;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(initializeDb){
            carService.importDataFromCsv();
            initializeDb = false;
        }
    }
}