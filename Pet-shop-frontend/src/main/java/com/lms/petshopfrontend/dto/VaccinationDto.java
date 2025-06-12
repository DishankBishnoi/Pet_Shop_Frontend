package org.example.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean available;
}


