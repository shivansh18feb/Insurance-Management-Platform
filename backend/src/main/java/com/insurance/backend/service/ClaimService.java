package com.insurance.backend.service;

import com.insurance.backend.dto.request.ClaimApprovalRequestDto;
import com.insurance.backend.dto.request.ClaimRequestDto;
import com.insurance.backend.dto.response.ClaimResponseDto;

import java.util.List;

public interface ClaimService {

    ClaimResponseDto createClaim(ClaimRequestDto requestDto);

    ClaimResponseDto updateClaim(Long claimId, ClaimRequestDto requestDto);

    void deleteClaim(Long claimId);

    ClaimResponseDto getClaimById(Long claimId);

    List<ClaimResponseDto> getAllClaims();

    List<ClaimResponseDto> getClaimsByPolicyId(Long policyId);

    ClaimResponseDto approveClaim(Long claimId,
                                  ClaimApprovalRequestDto requestDto);

}