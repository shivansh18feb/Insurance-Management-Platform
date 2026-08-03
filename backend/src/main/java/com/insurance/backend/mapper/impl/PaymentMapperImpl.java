package com.insurance.backend.mapper.impl;

import com.insurance.backend.dto.request.PaymentRequestDto;
import com.insurance.backend.dto.response.PaymentResponseDto;
import com.insurance.backend.entity.Payment;
import com.insurance.backend.mapper.PaymentMapper;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment toEntity(PaymentRequestDto requestDto) {

        if (requestDto == null) {
            return null;
        }

        return Payment.builder()
                .paymentAmount(requestDto.getPaymentAmount())
                .paymentMethod(requestDto.getPaymentMethod())
                .paymentDate(requestDto.getPaymentDate())
                .remarks(requestDto.getRemarks())
                .build();
    }

    @Override
    public PaymentResponseDto toResponseDto(Payment payment) {

        if (payment == null) {
            return null;
        }

        return PaymentResponseDto.builder()
                .paymentId(payment.getId())
                .paymentNumber(payment.getPaymentNumber())
                .claimId(payment.getClaim().getId())
                .paymentAmount(payment.getPaymentAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .remarks(payment.getRemarks())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}