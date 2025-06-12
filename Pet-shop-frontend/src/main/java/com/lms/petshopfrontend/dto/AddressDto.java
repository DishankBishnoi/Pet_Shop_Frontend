package org.example.frontend.Dto;

import lombok.Data;

@Data
public class AddressDto {
    private Integer id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
}
