package com.lms.petshopfrontend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @Column(name = "pet_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "breed")
    private String breed;

    @Column(name = "age")
    private Integer age;

    @Column(name = "price", precision = 38, scale = 2)
    private BigDecimal price;

    @Column(name = "description")
    private String description;

}