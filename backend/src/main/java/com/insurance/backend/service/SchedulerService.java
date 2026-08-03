package com.insurance.backend.service;

import com.insurance.backend.entity.Policy;
import com.insurance.backend.entity.PremiumSchedule;
import com.insurance.backend.entity.enums.NotificationType;
import com.insurance.backend.entity.enums.PolicyStatus;
import com.insurance.backend.entity.enums.PremiumStatus;
import com.insurance.backend.repository.PolicyRepository;
import com.insurance.backend.repository.PremiumScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final PremiumScheduleRepository premiumScheduleRepository;
    private final PolicyRepository policyRepository;
    private final NotificationService notificationService;

    // Run every day at 1:00 AM
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void checkOverduePremiums() {
        log.info("Running scheduled task: Check Overdue Premiums");
        List<PremiumSchedule> overdueSchedules = premiumScheduleRepository.findByStatusAndDueDateBefore(
                PremiumStatus.PENDING, LocalDate.now());

        for (PremiumSchedule ps : overdueSchedules) {
            ps.setStatus(PremiumStatus.OVERDUE);
            premiumScheduleRepository.save(ps);
            log.info("Marked premium {} as OVERDUE", ps.getPremiumNumber());
        }
    }

    // Run every day at 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void checkExpiringPolicies() {
        log.info("Running scheduled task: Check Expiring Policies");
        List<Policy> expiringPolicies = policyRepository.findByEndDateBefore(LocalDate.now());

        for (Policy policy : expiringPolicies) {
            if (policy.getStatus() == PolicyStatus.ACTIVE) {
                policy.setStatus(PolicyStatus.EXPIRED);
                policyRepository.save(policy);
                log.info("Marked policy {} as EXPIRED", policy.getPolicyNumber());
            }
        }
    }
}
