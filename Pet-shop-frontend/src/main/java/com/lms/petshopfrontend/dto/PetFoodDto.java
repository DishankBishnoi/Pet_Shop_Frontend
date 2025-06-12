package com.lms.petshopfrontend.dto;


import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PetFoodDto implements Serializable {

    private Integer id;

    private String name;

    private String brand;

    private String type;

    private Integer quantity;

    private BigDecimal price;

}