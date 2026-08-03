package com.insurance.backend.dto.request;

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
public class ClaimRequestDto {

    @NotNull(message = "Policy ID is required.")
    private Long policyId;

    @NotNull(message = "Claim amount is required.")
    @DecimalMin(value = "0.01", message = "Claim amount must be greater than zero.")
    private BigDecimal claimAmount;

    @NotBlank(message = "Claim reason is required.")
    private String claimReason;

    @NotNull(message = "Claim date is required.")
    private LocalDate claimDate;

}