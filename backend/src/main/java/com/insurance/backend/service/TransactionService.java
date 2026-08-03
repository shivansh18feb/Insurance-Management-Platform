package com.insurance.backend.service;

import com.insurance.backend.dto.response.TransactionResponseDto;

import java.util.List;

public interface TransactionService {
    List<TransactionResponseDto> getAllTransactions();
    List<TransactionResponseDto> getTransactionsByPolicy(Long policyId);
    List<TransactionResponseDto> getTransactionsByClaim(Long claimId);
}
