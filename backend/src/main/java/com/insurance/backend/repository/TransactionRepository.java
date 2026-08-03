package com.insurance.backend.repository;

import com.insurance.backend.entity.Transaction;
import com.insurance.backend.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionNumber(String transactionNumber);
    List<Transaction> findByInitiatedByIdOrderByCreatedAtDesc(Long userId);
    List<Transaction> findByTransactionType(TransactionType transactionType);
    List<Transaction> findByPolicyId(Long policyId);
    List<Transaction> findByClaimId(Long claimId);
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
}
