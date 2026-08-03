package com.insurance.backend.controller;

import com.insurance.backend.dto.response.ApiResponse;
import com.insurance.backend.dto.response.TransactionResponseDto;
import com.insurance.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponseDto>>> getAllTransactions() {
        return ResponseEntity.ok(ApiResponse.<List<TransactionResponseDto>>builder()
                .success(true)
                .message("Transactions fetched")
                .data(transactionService.getAllTransactions())
                .build());
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<TransactionResponseDto>>> getByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(ApiResponse.<List<TransactionResponseDto>>builder()
                .success(true)
                .message("Policy transactions fetched")
                .data(transactionService.getTransactionsByPolicy(policyId))
                .build());
    }

    @GetMapping("/claim/{claimId}")
    public ResponseEntity<ApiResponse<List<TransactionResponseDto>>> getByClaim(@PathVariable Long claimId) {
        return ResponseEntity.ok(ApiResponse.<List<TransactionResponseDto>>builder()
                .success(true)
                .message("Claim transactions fetched")
                .data(transactionService.getTransactionsByClaim(claimId))
                .build());
    }
}
