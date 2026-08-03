package com.insurance.backend.mapper;

import com.insurance.backend.dto.request.PaymentRequestDto;
import com.insurance.backend.dto.response.PaymentResponseDto;
import com.insurance.backend.entity.Payment;

public interface PaymentMapper {

    Payment toEntity(PaymentRequestDto requestDto);

    PaymentResponseDto toResponseDto(Payment payment);

}