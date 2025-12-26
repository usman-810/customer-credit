package com.creditcard.customer.feign.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponseDTO {
    private Long id;
    private String cardNumber;
    private Long customerId;
    private BigDecimal creditLimit;
    private BigDecimal availableCredit;
    private String cardType;
    private String status;
    private LocalDate expiryDate;
}