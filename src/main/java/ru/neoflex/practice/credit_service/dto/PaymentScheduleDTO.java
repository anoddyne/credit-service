package ru.neoflex.practice.credit_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentScheduleDTO (
        UUID id,
        LocalDate paymentDate,
        BigDecimal totalPayment,
        BigDecimal principalPayment,
        BigDecimal interestPayment,
        String status
) {
}
