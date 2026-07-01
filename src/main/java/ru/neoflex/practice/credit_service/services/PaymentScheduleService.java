package ru.neoflex.practice.credit_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.practice.credit_service.mappers.LoanMapper;
import ru.neoflex.practice.credit_service.models.Loan;
import ru.neoflex.practice.credit_service.models.PaymentSchedule;
import ru.neoflex.practice.credit_service.repositories.PaymentScheduleRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final LoanMapper loanMapper;

    public List<PaymentSchedule> generateAnnuitySchedule(BigDecimal amount, Integer terms, BigDecimal rate, Loan loan) {
        List<PaymentSchedule> schedule = new ArrayList<>();
        // TODO: Реализовать формулу аннуитета в цикле по количеству месяцев
        return schedule;
    }

    @Transactional
    public void payInstallment(UUID scheduleItemId) {
        // TODO: Логика погашения конкретного платежа по графику
    }
}
