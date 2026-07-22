package com.insurance.backend.repository;

import com.insurance.backend.entity.Policy;
import com.insurance.backend.entity.PolicyStatus;
import com.insurance.backend.entity.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    boolean existsByPolicyNumber(String policyNumber);

    Optional<Policy> findByPolicyNumber(String policyNumber);

    List<Policy> findByCustomerId(Long customerId);

    List<Policy> findByStatus(PolicyStatus status);

    List<Policy> findByPolicyType(PolicyType policyType);

    List<Policy> findByEndDateBefore(LocalDate date);

    Optional<Policy> findTopByOrderByIdDesc();
}