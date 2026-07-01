package ru.neoflex.practice.credit_service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.practice.credit_service.dto.LoanDetailsDTO;
import ru.neoflex.practice.credit_service.dto.PaymentScheduleDTO;
import ru.neoflex.practice.credit_service.models.Loan;
import ru.neoflex.practice.credit_service.models.PaymentSchedule;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    @Mapping(source = "status", target = "status")
    PaymentScheduleDTO toScheduleDto(PaymentSchedule paymentSchedule);

    @Mapping(source = "application.id", target = "loanApplicationId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "paymentScheduleList", target = "paymentScheduleList")
    LoanDetailsDTO toDetailsDto(Loan loan);
}
