package ru.neoflex.practice.credit_service.models.enums;

public enum ApplicationStatus {
    NEW("Новая заявка"),
    IN_PROGRESS("На проверке/скоринге"),
    APPROVED("Одобрена"),
    REJECTED("Отклонена");

    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }
}
