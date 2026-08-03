package com.insurance.backend.controller;

import com.insurance.backend.dto.request.PaymentRequestDto;
import com.insurance.backend.dto.response.PaymentResponseDto;
import com.insurance.backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(
            @Valid @RequestBody PaymentRequestDto requestDto) {

        PaymentResponseDto response =
                paymentService.createPayment(requestDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> updatePayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentRequestDto requestDto) {

        PaymentResponseDto response =
                paymentService.updatePayment(paymentId, requestDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(paymentId)
        );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }

    @GetMapping("/claim/{claimId}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByClaim(
            @PathVariable Long claimId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByClaim(claimId)
        );
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePayment(
            @PathVariable Long paymentId) {

        paymentService.deletePayment(paymentId);

        return ResponseEntity.ok("Payment deleted successfully.");
    }
}