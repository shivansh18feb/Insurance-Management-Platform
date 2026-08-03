package com.insurance.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.insurance.backend.entity.enums.PaymentMethod;
import com.insurance.backend.entity.enums.PaymentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private Long paymentId;

    private String paymentNumber;

    private Long claimId;

    private BigDecimal paymentAmount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDate paymentDate;

    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
