package ru.neoflex.practice.credit_service.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.practice.credit_service.dto.LoanApplication.LoanApplicationRequestDTO;
import ru.neoflex.practice.credit_service.dto.LoanApplication.LoanApplicationResponseDTO;
import ru.neoflex.practice.credit_service.mappers.LoanApplicationMapper;
import ru.neoflex.practice.credit_service.models.LoanApplication;
import ru.neoflex.practice.credit_service.models.LoanProduct;
import ru.neoflex.practice.credit_service.repositories.LoanApplicationRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanApplicationMapper loanApplicationMapper;

    @Transactional
    public LoanApplicationResponseDTO createApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        LoanApplication loanApplication = loanApplicationMapper.toEntity(loanApplicationRequestDTO);
        // LoanProduct loanProduct = loanApplication.getProduct();

        /* TODO: здесь дописать бизнес логику
            Достаем LoanProduct из базы по productId и проверяем: входит ли requestedAmount в диапазон minAmount–maxAmount, и подходит ли requestedTermMonths под minTermMonths–maxTermMonths. Если нет — выкидываем кастомное исключение, а не сохраняем заявку.
        * */

        return loanApplicationMapper.toResponseDto(loanApplication);
    }

    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDTO> getApplicationsByClientId(UUID id) {
        return loanApplicationMapper.toResponseDtoList(loanApplicationRepository.findByClientId(id));
    }

    @Transactional
    public void approveAndIssueLoan() {
        /*
        TODO: описать метод выдачи кредита
        1. Статус заявки меняется на APPROVED (или CLOSED, так как она выполнена).
        2. Рассчитывается график платежей (PaymentSchedule) на основе суммы, срока и процентной ставки продукта.
        3. Создается сам кредит (Loan) со статусом ACTIVE.
        4. Всё это одновременно сохраняется в базу. Если на этапе расчета графика произойдет ошибка, транзакция откатится, и база останется чистой.
         */
    }

    @Transactional(readOnly = true)
    public LoanApplicationResponseDTO getApplicationById(UUID id) {
        return loanApplicationRepository.findById(id)
                .map(loanApplicationMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Заявка с ID " + id + " не найдена"));
    }
}
