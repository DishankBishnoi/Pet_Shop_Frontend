package com.lms.petshopfrontend.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
@Value
public class TransactionManjotDto   {
    Integer id;
    LocalDate transactionDate;
    BigDecimal amount;
    String transactionStatus;
    String customerName;
    String petName;
}
