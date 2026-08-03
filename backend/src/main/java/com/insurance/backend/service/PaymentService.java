package com.insurance.backend.service;

import java.util.List;

import com.insurance.backend.dto.request.PaymentRequestDto;
import com.insurance.backend.dto.response.PaymentResponseDto;

public interface PaymentService {

    PaymentResponseDto createPayment(PaymentRequestDto requestDto);

    PaymentResponseDto updatePayment(Long paymentId, PaymentRequestDto requestDto);

    PaymentResponseDto getPaymentById(Long paymentId);

    List<PaymentResponseDto> getAllPayments();

    List<PaymentResponseDto> getPaymentsByClaim(Long claimId);

    void deletePayment(Long paymentId);
}