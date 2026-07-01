package ru.neoflex.practice.credit_service.mappers;


import org.mapstruct.Mapper;
import ru.neoflex.practice.credit_service.dto.LoanProduct.LoanProductResponseDTO;
import ru.neoflex.practice.credit_service.models.LoanProduct;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanProductMapper {

    LoanProductResponseDTO toResponseDto(LoanProduct loanProduct);

    List<LoanProductResponseDTO> toResponseDtoList(List<LoanProduct> loanProducts);
}
