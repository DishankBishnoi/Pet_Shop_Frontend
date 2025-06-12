package com.lms.petshopfrontend.dto;

import lombok.Data;

@Data
public class CustomerFullDto {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private AddressDto address;
}
