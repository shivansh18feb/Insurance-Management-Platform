package com.insurance.backend.service.impl;

import com.insurance.backend.dto.request.ClaimApprovalRequestDto;
import com.insurance.backend.dto.request.ClaimRequestDto;
import com.insurance.backend.dto.response.ClaimResponseDto;
import com.insurance.backend.entity.Claim;
import com.insurance.backend.entity.enums.ClaimStatus;
import com.insurance.backend.entity.Policy;
import com.insurance.backend.entity.enums.PolicyStatus;
import com.insurance.backend.exception.InvalidRequestException;
import com.insurance.backend.exception.ResourceNotFoundException;
import com.insurance.backend.mapper.ClaimMapper;
import com.insurance.backend.repository.ClaimRepository;
import com.insurance.backend.repository.PolicyRepository;
import com.insurance.backend.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@Transactional
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final ClaimMapper claimMapper;
    private static final Logger logger =
            LoggerFactory.getLogger(ClaimServiceImpl.class);

    @Override
    public ClaimResponseDto createClaim(ClaimRequestDto requestDto) {

        Policy policy = policyRepository.findById(requestDto.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with ID: " + requestDto.getPolicyId()));

        if (policy.getStatus() != PolicyStatus.ACTIVE) {
            throw new InvalidRequestException(
                    "Claim can only be created for an ACTIVE policy.");
        }

        if (requestDto.getClaimDate().isBefore(policy.getStartDate())) {
            throw new InvalidRequestException(
                    "Claim date cannot be before the policy start date.");
        }

        Claim claim = claimMapper.toEntity(requestDto);

        claim.setClaimNumber(generateClaimNumber());
        claim.setStatus(ClaimStatus.SUBMITTED);
        claim.setApprovedAmount(null);
        claim.setSettlementDate(null);
        claim.setPolicy(policy);

        claim = claimRepository.save(claim);

        return claimMapper.toResponseDto(claim);
    }

    @Override
    public ClaimResponseDto updateClaim(Long claimId, ClaimRequestDto requestDto) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Claim not found with ID: " + claimId));

        Policy policy = policyRepository.findById(requestDto.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with ID: " + requestDto.getPolicyId()));

        if (policy.getStatus() != PolicyStatus.ACTIVE) {
            throw new InvalidRequestException(
                    "Claim can only be updated for an ACTIVE policy.");
        }

        if (requestDto.getClaimDate().isBefore(policy.getStartDate())) {
            throw new InvalidRequestException(
                    "Claim date cannot be before the policy start date.");
        }

        claim.setClaimAmount(requestDto.getClaimAmount());
        claim.setClaimReason(requestDto.getClaimReason());
        claim.setClaimDate(requestDto.getClaimDate());
        claim.setPolicy(policy);

        claim = claimRepository.save(claim);

        return claimMapper.toResponseDto(claim);
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimResponseDto getClaimById(Long claimId) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Claim not found with ID: " + claimId));

        return claimMapper.toResponseDto(claim);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimResponseDto> getAllClaims() {

        return claimRepository.findAll()
                .stream()
                .map(claimMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimResponseDto> getClaimsByPolicyId(Long policyId) {

        return claimRepository.findByPolicyId(policyId)
                .stream()
                .map(claimMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteClaim(Long claimId) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Claim not found with ID: " + claimId));

        claimRepository.delete(claim);
    }

    private String generateClaimNumber() {

        long count = claimRepository.count() + 1;

        return String.format(
                "CLM-%d-%06d",
                Year.now().getValue(),
                count
        );
    }

    @Override
    public ClaimResponseDto approveClaim(Long claimId,
                                         ClaimApprovalRequestDto requestDto) {

        logger.info("Approving claim with ID: {}", claimId);

        Claim claim = getClaim(claimId);

        validateClaimForApproval(claim);

        claim.setApprovedAmount(requestDto.getApprovedAmount());
        claim.setStatus(ClaimStatus.APPROVED);

        claim = claimRepository.save(claim);

        logger.info("Claim {} approved successfully.", claimId);

        return claimMapper.toResponseDto(claim);
    }

    private void validateClaimForApproval(Claim claim) {

        if (claim.getStatus() != ClaimStatus.SUBMITTED) {
            throw new InvalidRequestException(
                    "Only submitted claims can be approved."
            );
        }
    }
    private Claim getClaim(Long claimId) {

        return claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Claim not found with ID: " + claimId));
    }
}