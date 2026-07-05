package ru.neoflex.practice.credit_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "loan_products")
public class LoanProduct {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Название продукта не должно быть пустым")
    private String name;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    @NotNull(message = "Процентная ставка обязательна")
    private BigDecimal interestRate;

    @Column(name = "min_amount", nullable = false)
    @NotNull(message = "Минимальная сумма обязательна")
    private BigDecimal minAmount;

    @Column(name = "max_amount", nullable = false)
    @NotNull(message = "Максимальная сумма обязательна")
    private BigDecimal maxAmount;

    @Column(name = "min_term_months", nullable = false)
    @Min(value = 1, message = "Минимальный срок должен быть от 1 месяца")
    private int minTermMonths;

    @Column(name = "max_term_months", nullable = false)
    @Min(value = 1, message = "Максимальный срок должен быть не менее 1 месяца")
    private int maxTermMonths;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }
}
