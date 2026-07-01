package ru.neoflex.practice.credit_service.models.enums;

import lombok.Getter;

@Getter
public enum LoanStatus {
    ACTIVE("Кредит выдан и активен"),
    OVERDUE("По кредиту имеется просроченная задолженность"),
    CLOSED("Кредит полностью погашен"),
    RESTRUCTURED("Условия кредита изменены (реструктуризация)"),
    CHARGE_OFF("Долг признан безнадежным и списан");

    private final String description;

    LoanStatus(String description) {
        this.description = description;
    }
}
