package com.foxminded.car.model;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name="car", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank
    private Long id;

    @Column(name = "objectId")
    @NotBlank
    @Size(min = 0, max = 11)
    private String objectId;

    @Column(name = "make")
    @NotBlank
    @Size(min = 0, max = 20)
    private String make;

    @Column(name = "productionYear")
    @NotBlank
    private int year;

    @Column(name = "model")
    @NotBlank
    @Size(min = 0, max = 30)
    private String model;

    @ElementCollection(targetClass = Category.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    @NotBlank
    private List<Category> category;

    public Car(String objectId, String make, int year, String model, List<Category> category) {
        this.objectId = objectId;
        this.make = make;
        this.year = year;
        this.model = model;
        this.category = category;
    }
}
