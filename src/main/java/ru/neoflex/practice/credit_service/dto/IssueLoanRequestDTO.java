package ru.neoflex.practice.credit_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record IssueLoanRequestDTO (
        @NotNull(message = "ID заявки обязателен для выдачи кредита")
        UUID applicationId,

        @NotNull(message = "ID счёта обязателен для выдачи кредита")
        UUID accountId
)
{
}
