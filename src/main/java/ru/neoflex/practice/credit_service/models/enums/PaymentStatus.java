package ru.neoflex.practice.credit_service.models.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    FUTURE("Запланирован"),
    PAID("Оплачен полностью"),
    PARTIALLY_PAID("Оплачен частично"),
    OVERDUE("Просрочен");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
