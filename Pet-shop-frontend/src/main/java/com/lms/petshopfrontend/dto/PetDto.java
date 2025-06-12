package com.lms.petshopfrontend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PetDto {
    private Integer id;
    private String name;
    private String breed;
    private Integer age;
    private BigDecimal price;
    private String description;
    private PetCategoryDto category;

    @Data
    public static class PetCategoryDto {
        private Integer id;
        private String name;
    }
}