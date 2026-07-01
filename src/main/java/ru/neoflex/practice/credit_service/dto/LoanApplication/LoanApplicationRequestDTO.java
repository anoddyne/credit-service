package ru.neoflex.practice.credit_service.dto.LoanApplication;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record LoanApplicationRequestDTO(
        @NotNull(message = "ID клиента обязательно")
        UUID clientId,

        @NotNull(message = "ID кредитного продукта обязательно")
        UUID productId,

        @NotNull(message = "Сумма кредита обязательна")
        @Positive(message = "Сумма кредита должна быть больше нуля")
        BigDecimal requestedAmount,

        @NotNull(message = "Срок кредита обязателен")
        @Min(value = 3, message = "Минимальный срок - 3 месяца")
        @Max(value = 360, message = "Максимальный срок - 360 месяцев")
        Integer requestedTermMonths
) {
}
