package com.insurance.backend.dto.response;

import com.insurance.backend.entity.enums.PaymentMethod;
import com.insurance.backend.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private String transactionNumber;
    private TransactionType transactionType;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime transactionDate;
    private String description;
    private Long policyId;
    private Long claimId;
    private Long premiumScheduleId;
}
