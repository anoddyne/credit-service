package ru.neoflex.practice.credit_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.practice.credit_service.dto.LoanProduct.LoanProductResponseDTO;
import ru.neoflex.practice.credit_service.services.LoanProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan_products")
public class LoanProductController {
    private final LoanProductService loanProductService;

    @GetMapping
    public ResponseEntity<List<LoanProductResponseDTO>> getProducts(@RequestParam(name = "isActive", defaultValue = "true") boolean isActive) {
        return ResponseEntity.ok(loanProductService.getAllProducts(isActive));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanProductResponseDTO> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(loanProductService.getProductById(id));
    }


}
