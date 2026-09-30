package ru.neoflex.practice.credit_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.practice.credit_service.dto.ErrorResponseDTO;
import ru.neoflex.practice.credit_service.dto.IssueLoanRequestDTO;
import ru.neoflex.practice.credit_service.dto.LoanDetailsDTO;
import ru.neoflex.practice.credit_service.services.LoanService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loans")
@Tag(name = "Кредиты", description = "Управление выданными кредитами, просмотр графиков и проведение платежей")
public class LoanController {
    private final LoanService loanService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить детальную информацию о кредите",
            description = "Возвращает кредит, включая остаток задолженности, процентную ставку и весь график платежей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о кредите получена"),
            @ApiResponse(responseCode = "404", description = "Кредит с указанным ID не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<LoanDetailsDTO> getLoanDetails(@PathVariable UUID id){
        return ResponseEntity.ok(loanService.getLoanDetails(id));
    }

    @PostMapping("/issue")
    @Operation(summary = "Выдать кредит по одобренной заявке",
            description = "Переводит заявку в финальный статус, создает кредит в статусе ACTIVE, генерирует аннуитетный график платежей и связывает его со счетом клиента.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Кредит выдан и график платежей сформирован"),
            @ApiResponse(responseCode = "400", description = "Валидация провалена или ошибка в расчетах",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Заявка на кредит не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<LoanDetailsDTO> issueLoan(@Valid @RequestBody IssueLoanRequestDTO requestDTO) {
        LoanDetailsDTO issuedLoan = loanService.approveAndIssueLoan(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(issuedLoan);
    }

    @PostMapping("/pay_installment/{installmentId}")
    @Operation(summary = "Оплатить конкретный взнос по графику",
            description = "Гасит выбранный ежемесячный платеж. Уменьшает остаток основного долга кредита. Если долг обнуляется, автоматически закрывает кредит.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Платеж успешно проведен, баланс кредита обновлен"),
            @ApiResponse(responseCode = "400", description = "Попытка оплатить уже погашенный взнос",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Строка платежа с ID не найдена в графике",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<Void> payInstallment(@PathVariable UUID installmentId) {
        loanService.payInstallment(installmentId);
        return ResponseEntity.ok().build();
    }
}
