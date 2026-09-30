package ru.neoflex.practice.credit_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import ru.neoflex.practice.credit_service.models.enums.ApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "loan_applications")
@NoArgsConstructor
public class LoanApplication {

    @UuidGenerator
    @Id
    @Column(name = "id")
    private UUID id;

    // здесь id появляется из внешних источников
    @Column(name = "client_id", nullable = false)
    @NotNull(message = "Идентификатор клиента обязателен")
    private UUID clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Кредитный продукт обязателен")
    private LoanProduct product;

    @Column(name = "requested_amount", nullable = false)
    @NotNull(message = "Запрошенная сумма обязательна")
    private BigDecimal requestedAmount;

    @Column(name = "requested_term_months", nullable = false)
    @Min(value = 1, message = "Срок запрошенного кредита должен быть не менее 1 месяца")
    private Integer requestedTermMonths;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", nullable = false)
    private ApplicationStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
