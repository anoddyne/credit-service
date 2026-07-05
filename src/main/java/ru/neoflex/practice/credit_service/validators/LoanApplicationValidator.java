package ru.neoflex.practice.credit_service.validators;


import org.springframework.stereotype.Component;
import ru.neoflex.practice.credit_service.dto.LoanApplication.LoanApplicationRequestDTO;
import ru.neoflex.practice.credit_service.exceptions.LoanValidationException;
import ru.neoflex.practice.credit_service.models.LoanProduct;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoanApplicationValidator {

    public void validate(LoanApplicationRequestDTO request, LoanProduct product) {
        List<String> errors = new ArrayList<>();

        if (request.requestedAmount().compareTo(product.getMinAmount()) < 0 ||
                request.requestedAmount().compareTo(product.getMaxAmount()) > 0) {
            errors.add(String.format("Запрошенная сумма %s выходит за рамки доступного диапазона [%s - %s]", request.requestedAmount(), product.getMinAmount(), product.getMaxAmount()));
        }

        if (request.requestedTermMonths().compareTo(product.getMinTermMonths()) < 0 ||
                request.requestedTermMonths().compareTo(product.getMaxTermMonths()) > 0) {
            errors.add(String.format("Запрошенный срок %s выходит за рамки доступного диапазона [%s - %s]", request.requestedTermMonths(), product.getMinTermMonths(), product.getMaxTermMonths()));
        }

        if (!errors.isEmpty()) {
            throw new LoanValidationException(String.join(" | ", errors));
        }
    }
}
