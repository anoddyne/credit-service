package ru.neoflex.practice.credit_service.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.practice.credit_service.dto.IssueLoanRequestDTO;
import ru.neoflex.practice.credit_service.dto.LoanDetailsDTO;
import ru.neoflex.practice.credit_service.exceptions.LoanValidationException;
import ru.neoflex.practice.credit_service.mappers.LoanMapper;
import ru.neoflex.practice.credit_service.models.Loan;
import ru.neoflex.practice.credit_service.models.LoanApplication;
import ru.neoflex.practice.credit_service.models.PaymentSchedule;
import ru.neoflex.practice.credit_service.models.enums.ApplicationStatus;
import ru.neoflex.practice.credit_service.models.enums.LoanStatus;
import ru.neoflex.practice.credit_service.models.enums.PaymentStatus;
import ru.neoflex.practice.credit_service.repositories.LoanApplicationRepository;
import ru.neoflex.practice.credit_service.repositories.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final PaymentScheduleService paymentScheduleService;
    private final LoanApplicationRepository loanApplicationRepository;

    @Transactional(readOnly = true)
    public LoanDetailsDTO getLoanDetails(UUID loanId) {
        return loanRepository.findById(loanId)
                .map(loanMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Кредит не найден"));
    }

    @Transactional
    public LoanDetailsDTO approveAndIssueLoan(IssueLoanRequestDTO request) {
        LoanApplication loanApplication = loanApplicationRepository.findById(request.applicationId()).orElseThrow(() -> new EntityNotFoundException("Заявка на кредит не найдена"));

        if (loanApplication.getStatus() != ApplicationStatus.NEW) {
            throw new LoanValidationException("Невозможно выдать кредит: заявка находится в статусе " + loanApplication.getStatus());
        }

        // Статус заявки меняется на APPROVED.
        loanApplication.setStatus(ApplicationStatus.APPROVED);

        // Создается сам кредит (Loan) со статусом ACTIVE.
        Loan loan = Loan.builder()
                .application(loanApplication)
                .accountId(request.accountId())
                .amount(loanApplication.getRequestedAmount())
                .balanceOwed(loanApplication.getRequestedAmount())
                .interestRate(loanApplication.getProduct().getInterestRate())
                .startDate(LocalDate.now())
                .status(LoanStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        // Рассчитывается график платежей (PaymentSchedule) на основе суммы, срока и процентной ставки продукта.
        List<PaymentSchedule> paymentScheduleList = paymentScheduleService.generateAnnuitySchedule(
                loanApplication.getRequestedAmount(),
                loanApplication.getRequestedTermMonths(),
                loanApplication.getProduct().getInterestRate(),
                loan
        );

        LocalDate finalPaymentDate = paymentScheduleList.getLast().getPaymentDate();

        loan.setPaymentScheduleList(paymentScheduleList);
        loan.setEndDate(finalPaymentDate);
        loanRepository.save(loan);

        return loanMapper.toDetailsDto(loan);
    }

    @Transactional
    public void payInstallment(UUID scheduleItemId) {
        PaymentSchedule installment = paymentScheduleService.findById(scheduleItemId);

        if (installment.getStatus() == PaymentStatus.PAID) {
            throw new LoanValidationException("Этот платеж уже оплачен");
        }

        installment.setStatus(PaymentStatus.PAID);
        installment.setUpdatedAt(LocalDateTime.now());

        Loan loan = installment.getLoan();
        BigDecimal newBalance = loan.getBalanceOwed().subtract(installment.getPrincipalPayment());

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO;
        }
        loan.setBalanceOwed(newBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.CLOSED);
        }

        loanRepository.save(loan);
    }
}
