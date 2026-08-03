package com.insurance.backend.service.impl;

import com.insurance.backend.dto.response.PremiumScheduleResponseDto;
import com.insurance.backend.entity.Policy;
import com.insurance.backend.entity.PremiumSchedule;
import com.insurance.backend.entity.enums.PaymentMethod;
import com.insurance.backend.entity.enums.PremiumStatus;
import com.insurance.backend.entity.enums.TransactionType;
import com.insurance.backend.entity.Transaction;
import com.insurance.backend.exception.InvalidRequestException;
import com.insurance.backend.exception.ResourceNotFoundException;
import com.insurance.backend.repository.PolicyRepository;
import com.insurance.backend.repository.PremiumScheduleRepository;
import com.insurance.backend.repository.TransactionRepository;
import com.insurance.backend.service.PremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PremiumServiceImpl implements PremiumService {

    private final PremiumScheduleRepository premiumScheduleRepository;
    private final PolicyRepository policyRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public List<PremiumScheduleResponseDto> generatePremiumSchedule(Long policyId, int numberOfInstallments) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + policyId));

        if (numberOfInstallments <= 0) numberOfInstallments = 12; // default monthly

        BigDecimal installmentAmount = policy.getPremiumAmount().divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);
        List<PremiumSchedule> schedules = new ArrayList<>();
        LocalDate startDate = policy.getStartDate();

        for (int i = 1; i <= numberOfInstallments; i++) {
            PremiumSchedule schedule = PremiumSchedule.builder()
                    .policy(policy)
                    .dueDate(startDate.plusMonths(i - 1))
                    .amount(installmentAmount)
                    .status(PremiumStatus.PENDING)
                    .installmentNumber(i)
                    .premiumNumber("PRM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                    .build();
            schedules.add(schedule);
        }

        return premiumScheduleRepository.saveAll(schedules).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PremiumScheduleResponseDto> getPolicySchedule(Long policyId) {
        return premiumScheduleRepository.findByPolicyIdOrderByDueDateAsc(policyId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PremiumScheduleResponseDto payPremium(Long premiumScheduleId, String paymentMethodStr, String remarks) {
        PremiumSchedule schedule = premiumScheduleRepository.findById(premiumScheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Premium schedule not found: " + premiumScheduleId));

        if (schedule.getStatus() == PremiumStatus.PAID) {
            throw new InvalidRequestException("Premium is already paid");
        }

        schedule.setStatus(PremiumStatus.PAID);
        schedule.setPaidDate(LocalDate.now());
        schedule.setRemarks(remarks);

        PremiumSchedule updated = premiumScheduleRepository.save(schedule);

        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(paymentMethodStr.toUpperCase());
        } catch (Exception e) {
            method = PaymentMethod.UPI;
        }

        // Record Transaction
        Transaction transaction = Transaction.builder()
                .transactionNumber("TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .transactionType(TransactionType.PREMIUM_PAYMENT)
                .amount(schedule.getAmount())
                .paymentMethod(method)
                .transactionDate(LocalDateTime.now())
                .description("Premium payment for schedule: " + schedule.getPremiumNumber())
                .policyId(schedule.getPolicy().getId())
                .premiumScheduleId(schedule.getId())
                .build();
        transactionRepository.save(transaction);

        return mapToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PremiumScheduleResponseDto> getOverduePremiums() {
        return premiumScheduleRepository.findByStatusAndDueDateBefore(PremiumStatus.PENDING, LocalDate.now()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PremiumScheduleResponseDto mapToDto(PremiumSchedule ps) {
        return PremiumScheduleResponseDto.builder()
                .id(ps.getId())
                .policyId(ps.getPolicy().getId())
                .premiumNumber(ps.getPremiumNumber())
                .dueDate(ps.getDueDate())
                .amount(ps.getAmount())
                .status(ps.getStatus())
                .paidDate(ps.getPaidDate())
                .installmentNumber(ps.getInstallmentNumber())
                .remarks(ps.getRemarks())
                .build();
    }
}
