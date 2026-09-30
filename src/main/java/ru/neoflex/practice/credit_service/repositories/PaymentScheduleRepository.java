package ru.neoflex.practice.credit_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.practice.credit_service.models.PaymentSchedule;
import ru.neoflex.practice.credit_service.models.enums.PaymentStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, UUID> {
    List<PaymentSchedule> findByPaymentDateLessThanEqualAndStatus(LocalDate date, PaymentStatus status);
}
