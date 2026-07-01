package ru.neoflex.practice.credit_service.dto.LoanApplication;

import ru.neoflex.practice.credit_service.dto.LoanProduct.LoanProductResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record LoanApplicationResponseDTO(
        UUID id,
        UUID clientId,
        LoanProductResponseDTO product,
        BigDecimal requestedAmount,
        Integer requestedTermMonths,
        String status,
        LocalDateTime createdAt
) {
}
