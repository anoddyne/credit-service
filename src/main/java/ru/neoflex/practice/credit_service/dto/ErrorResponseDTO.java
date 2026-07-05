package ru.neoflex.practice.credit_service.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        String error,
        int status,
        LocalDateTime timestamp
) {
    public ErrorResponseDTO(String message, String error, int status) {
        this(message, error, status, LocalDateTime.now());
    }
}
