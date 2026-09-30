package ru.neoflex.practice.credit_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LoanDetailsDTO(
        UUID id,
        UUID loanApplicationId,
        UUID accountId,
        BigDecimal amount,
        BigDecimal balanceOwed,
        BigDecimal interestRate,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        LocalDateTime createdAt,
        List<PaymentScheduleDTO> paymentScheduleList
) {
}
