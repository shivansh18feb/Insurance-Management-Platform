package com.insurance.backend.dto.request;

import com.insurance.backend.entity.PolicyStatus;
import com.insurance.backend.entity.PolicyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyRequestDto {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotBlank(message = "Policy name is required")
    private String policyName;

    @NotNull(message = "Policy type is required")
    private PolicyType policyType;

    @NotNull(message = "Coverage amount is required")
    @DecimalMin(value = "0.01", message = "Coverage amount must be greater than zero")
    private BigDecimal coverageAmount;

    @NotNull(message = "Premium amount is required")
    @DecimalMin(value = "0.01", message = "Premium amount must be greater than zero")
    private BigDecimal premiumAmount;

    @NotNull(message = "Policy start date is required")
    private LocalDate startDate;

    @NotNull(message = "Policy end date is required")
    private LocalDate endDate;

    @NotNull(message = "Policy status is required")
    private PolicyStatus status;
}