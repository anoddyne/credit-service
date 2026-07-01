package ru.neoflex.practice.credit_service.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.practice.credit_service.dto.LoanProduct.LoanProductResponseDTO;
import ru.neoflex.practice.credit_service.mappers.LoanProductMapper;
import ru.neoflex.practice.credit_service.models.LoanProduct;
import ru.neoflex.practice.credit_service.repositories.LoanProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanProductService {
    private final LoanProductRepository loanProductRepository;
    private final LoanProductMapper loanProductMapper;

    @Transactional(readOnly = true)
    public List<LoanProductResponseDTO> getAllProducts(Boolean isActive) {
        List<LoanProduct> products;
        if (isActive == null) {
            products = loanProductRepository.findAll();
        } else {
            products = loanProductRepository.findByIsActive(isActive);
        }
        return loanProductMapper.toResponseDtoList(products);
    }

    @Transactional(readOnly = true)
    public LoanProductResponseDTO getProductById(UUID id) {
        return loanProductRepository.findById(id).map(loanProductMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("Кредитный продукт с ID " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public LoanProduct findProductById(UUID id) {
        return loanProductRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Кредитный продукт с ID " + id + " не найден"));
    }

}
