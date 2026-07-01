package ru.neoflex.practice.credit_service.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.practice.credit_service.dto.LoanApplication.LoanApplicationRequestDTO;
import ru.neoflex.practice.credit_service.dto.LoanApplication.LoanApplicationResponseDTO;
import ru.neoflex.practice.credit_service.services.LoanApplicationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan_applications")
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    @PostMapping
    public ResponseEntity<LoanApplicationResponseDTO> createApplication(@Valid @RequestBody LoanApplicationRequestDTO requestDTO) {
        LoanApplicationResponseDTO response = loanApplicationService.createApplication(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<LoanApplicationResponseDTO>> getApplicationsByClient(@PathVariable UUID clientId) {
        return ResponseEntity.ok(loanApplicationService.getApplicationsByClientId(clientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationResponseDTO> getApplicationById(@PathVariable UUID id) {
        return ResponseEntity.ok(loanApplicationService.getApplicationById(id));
    }

}
