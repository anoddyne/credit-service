package ru.neoflex.practice.credit_service.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.practice.credit_service.dto.IssueLoanRequestDTO;
import ru.neoflex.practice.credit_service.dto.LoanDetailsDTO;
import ru.neoflex.practice.credit_service.services.LoanService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loans")
public class LoanController {
    private final LoanService loanService;

    @GetMapping("/{id}")
    public ResponseEntity<LoanDetailsDTO> getLoanDetails(@PathVariable UUID id){
        return ResponseEntity.ok(loanService.getLoanDetails(id));
    }

    @PostMapping("/issue")
    public ResponseEntity<LoanDetailsDTO> issueLoan(@Valid @RequestBody IssueLoanRequestDTO requestDTO) {
        LoanDetailsDTO issuedLoan = loanService.issueLoan(requestDTO.applicationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(issuedLoan);
    }
}
