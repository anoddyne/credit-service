package ru.neoflex.practice.credit_service.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.practice.credit_service.dto.IssueLoanRequestDTO;
import ru.neoflex.practice.credit_service.dto.LoanDetailsDTO;
import ru.neoflex.practice.credit_service.mappers.LoanMapper;
import ru.neoflex.practice.credit_service.models.Loan;
import ru.neoflex.practice.credit_service.models.LoanApplication;
import ru.neoflex.practice.credit_service.models.PaymentSchedule;
import ru.neoflex.practice.credit_service.models.enums.LoanStatus;
import ru.neoflex.practice.credit_service.repositories.LoanApplicationRepository;
import ru.neoflex.practice.credit_service.repositories.LoanRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final PaymentScheduleService paymentScheduleService;
    private final LoanApplicationRepository loanApplicationRepository;

    @Transactional
    public LoanDetailsDTO issueLoan(IssueLoanRequestDTO requestDTO) {
        LoanApplication loanApplication = loanApplicationRepository.findById(requestDTO.applicationId()).orElseThrow(() -> new EntityNotFoundException("Заявка на кредит не найдена"));
        Loan loan = Loan.builder()
                .application(loanApplication)
                .accountId(requestDTO.accountId())
                .amount(loanApplication.getRequestedAmount())
                .balanceOwed(loanApplication.getRequestedAmount())
                .status(LoanStatus.ACTIVE)
                .build();

        List<PaymentSchedule> schedule = paymentScheduleService.generateAnnuitySchedule(
                loan.getAmount(),
                loanApplication.getRequestedTermMonths(),
                loanApplication.getProduct().getInterestRate(),
                loan
        );

        loan.setPaymentScheduleList(schedule);

        return loanMapper.toDetailsDto(loanRepository.save(loan));
    }

    @Transactional(readOnly = true)
    public LoanDetailsDTO getLoanDetails(UUID loanId) {
        return loanRepository.findById(loanId)
                .map(loanMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Кредит не найден"));
    }
}
