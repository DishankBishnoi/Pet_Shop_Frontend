package org.example.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String position;
    private LocalDate hireDate;
    private String phoneNumber;
    private String email;
}
