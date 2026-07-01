package ru.neoflex.practice.credit_service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.practice.credit_service.dto.LoanApplication.LoanApplicationRequestDTO;
import ru.neoflex.practice.credit_service.dto.LoanApplication.LoanApplicationResponseDTO;
import ru.neoflex.practice.credit_service.models.LoanApplication;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LoanApplication toEntity(LoanApplicationRequestDTO requestDTO);

    @Mapping(source = "status", target = "status")
    LoanApplicationResponseDTO toResponseDto(LoanApplication loanApplication);

    @Mapping(source = "status", target = "status")
    List<LoanApplicationResponseDTO> toResponseDtoList(List<LoanApplication> loanApplicationList);
}
