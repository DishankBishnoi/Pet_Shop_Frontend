package com.lms.petshopfrontend.dto;

import lombok.Data;

@Data
public class SupplierDto {
    private Integer id;
    private String name;
    private String contactPerson;
    private String phoneNumber;
    private String email;
    private String city;
    private String state;
}
