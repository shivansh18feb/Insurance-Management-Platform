package com.insurance.backend.service.impl;

import com.insurance.backend.dto.request.PaymentRequestDto;
import com.insurance.backend.dto.response.PaymentResponseDto;
import com.insurance.backend.entity.Claim;
import com.insurance.backend.entity.Payment;
import com.insurance.backend.entity.enums.ClaimStatus;
import com.insurance.backend.entity.enums.PaymentStatus;
import com.insurance.backend.exception.InvalidRequestException;
import com.insurance.backend.exception.ResourceNotFoundException;
import com.insurance.backend.mapper.PaymentMapper;
import com.insurance.backend.repository.ClaimRepository;
import com.insurance.backend.repository.PaymentRepository;
import com.insurance.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final ClaimRepository claimRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {

        logger.info("Creating payment for claim ID: {}", requestDto.getClaimId());

        Claim claim = getClaim(requestDto.getClaimId());

        validateClaimForPayment(claim);

        validateDuplicatePayment(claim.getId());

        validatePaymentAmount(requestDto, claim);

        validatePaymentDate(requestDto, claim);

        Payment payment = buildPayment(requestDto, claim);

        payment = paymentRepository.save(payment);

        updateClaimStatus(claim);

        logger.info("Payment {} created successfully.",
                payment.getPaymentNumber());

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional
    public PaymentResponseDto updatePayment(Long paymentId,
                                            PaymentRequestDto requestDto) {

        logger.info("Updating payment with ID: {}", paymentId);

        Payment payment = getPayment(paymentId);

        validatePaymentForUpdate(payment);

        validatePaymentAmount(requestDto, payment.getClaim());

        validatePaymentDate(requestDto, payment.getClaim());

        updatePaymentDetails(payment, requestDto);

        payment = paymentRepository.saveAndFlush(payment);

        logger.info("Payment {} updated successfully.", payment.getPaymentNumber());

        return paymentMapper.toResponseDto(payment);
    }

    private void validatePaymentForUpdate(Payment payment) {

        if (payment.getClaim().getStatus() == ClaimStatus.SETTLED) {
            throw new InvalidRequestException(
                    "Settled payments cannot be modified.");
        }
    }

    private void updatePaymentDetails(Payment payment,
                                      PaymentRequestDto requestDto) {

        payment.setPaymentAmount(requestDto.getPaymentAmount());
        payment.setPaymentMethod(requestDto.getPaymentMethod());
        payment.setPaymentDate(requestDto.getPaymentDate());
        payment.setRemarks(requestDto.getRemarks());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(Long paymentId) {

        logger.info("Fetching payment with ID: {}", paymentId);

        return paymentMapper.toResponseDto(getPayment(paymentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getAllPayments() {

        logger.info("Fetching all payments.");

        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByClaim(Long claimId) {

        logger.info("Fetching payments for Claim ID: {}", claimId);

        return paymentRepository.findByClaimId(claimId)
                .stream()
                .map(paymentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePayment(Long paymentId) {

        logger.info("Deleting payment with ID: {}", paymentId);

        Payment payment = getPayment(paymentId);

        throw new InvalidRequestException(
                "Payment cannot be deleted because the associated claim has already been settled.");
    }

    private Claim getClaim(Long claimId) {

        logger.debug("Fetching claim with ID: {}", claimId);

        return claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Claim not found with ID: " + claimId));
    }

    private void validateClaimForPayment(Claim claim) {

        if (claim.getStatus() != ClaimStatus.APPROVED) {
            throw new InvalidRequestException(
                    "Payment can only be created for APPROVED claims.");
        }

        if (claim.getApprovedAmount() == null) {
            throw new InvalidRequestException(
                    "Approved amount is not available for this claim.");
        }
    }

    private void validateDuplicatePayment(Long claimId) {

        if (paymentRepository.existsByClaimId(claimId)) {
            throw new InvalidRequestException(
                    "Payment already exists for Claim ID: " + claimId);
        }
    }

    private void validatePaymentAmount(PaymentRequestDto requestDto,
                                       Claim claim) {

        if (requestDto.getPaymentAmount()
                .compareTo(claim.getApprovedAmount()) > 0) {

            throw new InvalidRequestException(
                    "Payment amount cannot exceed the approved claim amount.");
        }
    }

    private void validatePaymentDate(PaymentRequestDto requestDto,
                                     Claim claim) {

        if (requestDto.getPaymentDate() != null
                && requestDto.getPaymentDate().isBefore(claim.getClaimDate())) {

            throw new InvalidRequestException(
                    "Payment date cannot be before the claim date.");
        }
    }

    private Payment buildPayment(PaymentRequestDto requestDto, Claim claim) {

        Payment payment = paymentMapper.toEntity(requestDto);

        payment.setClaim(claim);
        payment.setPaymentNumber(generatePaymentNumber());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        return payment;
    }

    private void updateClaimStatus(Claim claim) {

        logger.debug("Updating claim {} status to SETTLED",
                claim.getId());

        claim.setStatus(ClaimStatus.SETTLED);

        claimRepository.save(claim);
    }

    private Payment getPayment(Long paymentId) {

        logger.debug("Fetching payment with ID: {}", paymentId);

        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with ID: " + paymentId));
    }

    private String generatePaymentNumber() {

        Optional<Payment> lastPayment =
                paymentRepository.findTopByOrderByIdDesc();

        long nextNumber = lastPayment
                .map(payment -> payment.getId() + 1)
                .orElse(1L);

        return String.format(
                "PAY-%d-%06d",
                Year.now().getValue(),
                nextNumber
        );
    }
}