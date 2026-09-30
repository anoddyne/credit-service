package ru.neoflex.practice.credit_service.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.practice.credit_service.models.Loan;
import ru.neoflex.practice.credit_service.models.PaymentSchedule;
import ru.neoflex.practice.credit_service.models.enums.PaymentStatus;
import ru.neoflex.practice.credit_service.repositories.PaymentScheduleRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {
    private final PaymentScheduleRepository paymentScheduleRepository;

    public List<PaymentSchedule> generateAnnuitySchedule(BigDecimal amount, Integer terms, BigDecimal rate, Loan loan) {
        List<PaymentSchedule> schedule = new ArrayList<>();

        double monthlyRate = rate.doubleValue() / 12.0 / 100.0;

        double annuityCoefficient = (monthlyRate * Math.pow(1 + monthlyRate, terms)) / (Math.pow(1 + monthlyRate, terms) - 1);

        BigDecimal totalPayment = amount.multiply(BigDecimal.valueOf(annuityCoefficient).setScale(2, RoundingMode.HALF_UP));

        BigDecimal remainingDebt = amount;

        LocalDate firstPaymentDate = loan.getStartDate() != null ? loan.getStartDate() : LocalDate.now();

        for (int i = 1; i <= terms; i++) {
            BigDecimal interestPayment = remainingDebt.multiply(BigDecimal.valueOf(monthlyRate)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal principalPayment = totalPayment.subtract(interestPayment);

            if (i == terms) {
                principalPayment = remainingDebt;
                totalPayment = principalPayment.add(interestPayment);
                remainingDebt = BigDecimal.ZERO;
            } else {
                remainingDebt = remainingDebt.subtract(principalPayment);
            }

            PaymentSchedule payment = PaymentSchedule.builder()
                    .loan(loan)
                    .paymentDate(firstPaymentDate.plusMonths(i))
                    .interestPayment(interestPayment)
                    .principalPayment(principalPayment)
                    .totalPayment(totalPayment)
                    .status(PaymentStatus.FUTURE)
                    .createdAt(LocalDateTime.now())
                    .build();
            schedule.add(payment);
        }
        return schedule;
    }

    public PaymentSchedule findById(UUID installmentId) {
        return paymentScheduleRepository.findById(installmentId).orElseThrow(() -> new EntityNotFoundException("Платеж в графике не найден"));
    }
}
