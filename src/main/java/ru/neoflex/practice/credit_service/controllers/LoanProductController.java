package ru.neoflex.practice.credit_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.practice.credit_service.dto.ErrorResponseDTO;
import ru.neoflex.practice.credit_service.dto.LoanProduct.LoanProductResponseDTO;
import ru.neoflex.practice.credit_service.services.LoanProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan_products")
@Tag(name = "Кредитные продукты", description = "Просмотр каталога доступных кредитных продуктов банка и их условий")
public class LoanProductController {
    private final LoanProductService loanProductService;

    @GetMapping
    @Operation(summary = "Получить список кредитных продуктов",
            description = "Возвращает список всех кредитных продуктов банка. По умолчанию возвращает только активные (доступные для оформления) продукты.")
    @ApiResponse(responseCode = "200", description = "Список кредитных продуктов успешно получен")
    public ResponseEntity<List<LoanProductResponseDTO>> getProducts(@RequestParam(name = "isActive", defaultValue = "true") boolean isActive) {
        return ResponseEntity.ok(loanProductService.getAllProducts(isActive));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о конкретном продукте по ID",
            description = "Возвращает условия кредитного продукта.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Кредитный продукт найден"),
            @ApiResponse(responseCode = "404", description = "Продукт с ID не существует в каталоге",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<LoanProductResponseDTO> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(loanProductService.getProductById(id));
    }
}
