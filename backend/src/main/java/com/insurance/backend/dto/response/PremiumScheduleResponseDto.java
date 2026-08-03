package com.insurance.backend.dto.response;

import com.insurance.backend.entity.enums.PremiumStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumScheduleResponseDto {
    private Long id;
    private Long policyId;
    private String premiumNumber;
    private LocalDate dueDate;
    private BigDecimal amount;
    private PremiumStatus status;
    private LocalDate paidDate;
    private Integer installmentNumber;
    private String remarks;
}
