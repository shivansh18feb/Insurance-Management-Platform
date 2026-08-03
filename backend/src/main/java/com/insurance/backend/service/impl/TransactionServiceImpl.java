package com.insurance.backend.service.impl;

import com.insurance.backend.dto.response.TransactionResponseDto;
import com.insurance.backend.entity.Transaction;
import com.insurance.backend.repository.TransactionRepository;
import com.insurance.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionResponseDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByPolicy(Long policyId) {
        return transactionRepository.findByPolicyId(policyId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByClaim(Long claimId) {
        return transactionRepository.findByClaimId(claimId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TransactionResponseDto mapToDto(Transaction t) {
        return TransactionResponseDto.builder()
                .id(t.getId())
                .transactionNumber(t.getTransactionNumber())
                .transactionType(t.getTransactionType())
                .amount(t.getAmount())
                .paymentMethod(t.getPaymentMethod())
                .transactionDate(t.getTransactionDate())
                .description(t.getDescription())
                .policyId(t.getPolicyId())
                .claimId(t.getClaimId())
                .premiumScheduleId(t.getPremiumScheduleId())
                .build();
    }
}
