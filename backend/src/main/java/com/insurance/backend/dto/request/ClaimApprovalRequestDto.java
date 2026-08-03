package com.insurance.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClaimApprovalRequestDto {

    @NotNull(message = "Approved amount is required.")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Approved amount must be greater than zero.")
    private BigDecimal approvedAmount;
}