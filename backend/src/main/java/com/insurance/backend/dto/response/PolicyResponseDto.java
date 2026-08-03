package com.insurance.backend.dto.response;

import com.insurance.backend.entity.enums.PolicyStatus;
import com.insurance.backend.entity.enums.PolicyType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyResponseDto {

    private Long id;

    private String policyNumber;

    private Long customerId;

    private String customerFullName;

    private String policyName;

    private PolicyType policyType;

    private BigDecimal coverageAmount;

    private BigDecimal premiumAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private PolicyStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}