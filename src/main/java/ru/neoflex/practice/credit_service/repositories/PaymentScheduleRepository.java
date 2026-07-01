package ru.neoflex.practice.credit_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.practice.credit_service.models.PaymentSchedule;

import java.util.UUID;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, UUID> {
}
