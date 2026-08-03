package com.insurance.backend.repository;

import com.insurance.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByClaimId(Long claimId);

    Optional<Payment> findTopByOrderByIdDesc();

    boolean existsByPaymentNumber(String paymentNumber);

    boolean existsByClaimId(Long claimId);

    Optional<Payment> findByPaymentNumber(String paymentNumber);
}