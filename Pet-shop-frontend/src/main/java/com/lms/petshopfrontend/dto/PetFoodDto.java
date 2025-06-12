package com.lms.petshopfrontend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PetFoodDto implements Serializable {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String brand;

    @Size(max = 255)
    private String type;

    private Integer quantity;

    private BigDecimal price;


}
