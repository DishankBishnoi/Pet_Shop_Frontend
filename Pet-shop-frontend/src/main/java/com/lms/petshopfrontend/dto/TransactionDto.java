package com.lms.petshopfrontend.dto;

import lombok.Data;

@Data
public class TransactionDto {
    private Integer id;
    private String transactionDate;
    private double amount;
    private String transactionStatus;
}
