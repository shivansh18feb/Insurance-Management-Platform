package com.insurance.backend.repository;

import com.insurance.backend.entity.PremiumSchedule;
import com.insurance.backend.entity.enums.PremiumStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumScheduleRepository extends JpaRepository<PremiumSchedule, Long> {
    List<PremiumSchedule> findByPolicyIdOrderByDueDateAsc(Long policyId);
    List<PremiumSchedule> findByPolicyIdAndStatus(Long policyId, PremiumStatus status);
    List<PremiumSchedule> findByStatusAndDueDateBefore(PremiumStatus status, LocalDate date);
    Optional<PremiumSchedule> findByPremiumNumber(String premiumNumber);
    long countByStatus(PremiumStatus status);
}
