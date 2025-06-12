package com.lms.petshopfrontend.dto;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroomingServiceDto implements Serializable {
    Integer id;
    String name;
    String description;
    BigDecimal price;
    Boolean available;
}
