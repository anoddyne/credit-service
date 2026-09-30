package ru.neoflex.practice.credit_service.controllers;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.practice.credit_service.dto.ErrorResponseDTO;
import ru.neoflex.practice.credit_service.dto.LoanApplication.LoanApplicationRequestDTO;
import ru.neoflex.practice.credit_service.dto.LoanApplication.LoanApplicationResponseDTO;
import ru.neoflex.practice.credit_service.services.LoanApplicationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan_applications")
@Tag(name = "Заявки на кредит", description = "Управление кредитными заявками")
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    @PostMapping
    @Operation(summary = "Подать новую заявку на кредит",
            description = "Создает заявку в статусе NEW после проверки суммы и срока на соответствие условиям продукта.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заявка успешно создана"),
            @ApiResponse(responseCode = "400", description = "Бизнес-валидация не пройдена (сумма/срок вне диапазона)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Кредитный продукт с указанным ID не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<LoanApplicationResponseDTO> createApplication(@Valid @RequestBody LoanApplicationRequestDTO requestDTO) {
        LoanApplicationResponseDTO response = loanApplicationService.createApplication(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Получить все заявки по клиенту",
            description = "Возвращает список всех кредитных заявок пользователя по UUID.")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<LoanApplicationResponseDTO>> getApplicationsByClient(@PathVariable UUID clientId) {
        return ResponseEntity.ok(loanApplicationService.getApplicationsByClientId(clientId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить детали заявки по ID",
            description = "Возвращает полную информацию по заявке на кредит.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о заявке найдена"),
            @ApiResponse(responseCode = "404", description = "Заявка с таким ID не существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<LoanApplicationResponseDTO> getApplicationById(@PathVariable UUID id) {
        return ResponseEntity.ok(loanApplicationService.getApplicationById(id));
    }

}
