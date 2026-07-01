package ru.neoflex.practice.credit_service.dto.LoanProduct;


import java.math.BigDecimal;
import java.util.UUID;

public record LoanProductResponseDTO (
        UUID id,
        String name,
        BigDecimal interestRate,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        int minTermMonths,
        int maxTermMonths,
        boolean isActive
) {}
