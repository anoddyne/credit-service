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
import ru.neoflex.practice.credit_service.models.enums.ApplicationStatus;
import ru.neoflex.practice.credit_service.repositories.LoanApplicationRepository;
import ru.neoflex.practice.credit_service.validators.LoanApplicationValidator;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanProductService loanProductService;
    private final LoanApplicationMapper loanApplicationMapper;
    private final LoanApplicationValidator loanApplicationValidator;

    @Transactional
    public LoanApplicationResponseDTO createApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        LoanProduct loanProduct = loanProductService.findProductById(loanApplicationRequestDTO.productId());

        loanApplicationValidator.validate(loanApplicationRequestDTO, loanProduct);

        LoanApplication loanApplication = loanApplicationMapper.toEntity(loanApplicationRequestDTO);
        loanApplication.setProduct(loanProduct);
        loanApplication.setStatus(ApplicationStatus.NEW);

        return loanApplicationMapper.toResponseDto(loanApplicationRepository.save(loanApplication));
    }

    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDTO> getApplicationsByClientId(UUID id) {
        return loanApplicationMapper.toResponseDtoList(loanApplicationRepository.findByClientId(id));
    }

    @Transactional(readOnly = true)
    public LoanApplicationResponseDTO getApplicationById(UUID id) {
        return loanApplicationRepository.findById(id)
                .map(loanApplicationMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Заявка с ID " + id + " не найдена"));
    }
}
