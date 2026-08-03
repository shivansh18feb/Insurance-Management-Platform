package com.insurance.backend.dto.response;

import com.insurance.backend.entity.enums.ClaimStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimResponseDto {

    private Long claimId;

    private String claimNumber;

    private BigDecimal claimAmount;

    private BigDecimal approvedAmount;

    private String claimReason;

    private LocalDate claimDate;

    private LocalDate settlementDate;

    private ClaimStatus status;

    private Long policyId;

}