package ru.neoflex.practice.credit_service.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.practice.credit_service.models.Loan;
import ru.neoflex.practice.credit_service.models.PaymentSchedule;
import ru.neoflex.practice.credit_service.models.enums.LoanStatus;
import ru.neoflex.practice.credit_service.models.enums.PaymentStatus;
import ru.neoflex.practice.credit_service.repositories.PaymentScheduleRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanSchedulerService {
    private final PaymentScheduleRepository paymentScheduleRepository;

    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    public void checkOverduePayments() {
        log.info("Запуск проверки просроченных платежей.");
        LocalDate today = LocalDate.now();

        List<PaymentSchedule> overdueInstallments = paymentScheduleRepository.findByPaymentDateLessThanEqualAndStatus(today, PaymentStatus.FUTURE);

        for (PaymentSchedule installment : overdueInstallments) {
            installment.setStatus(PaymentStatus.OVERDUE);

            Loan loan = installment.getLoan();
            if (loan.getStatus() != LoanStatus.OVERDUE) {
                loan.setStatus(LoanStatus.OVERDUE);
                log.warn("Кредит: {} переведен в статус Просрочен", loan.getId());
            }
        }

        log.info("Проверка завершена. Обработано просроченных платежей: {}", overdueInstallments.size());
    }

}
