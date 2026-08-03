package com.insurance.backend.mapper.impl;

import com.insurance.backend.dto.request.ClaimRequestDto;
import com.insurance.backend.dto.response.ClaimResponseDto;
import com.insurance.backend.entity.Claim;
import com.insurance.backend.mapper.ClaimMapper;
import org.springframework.stereotype.Component;

@Component
public class ClaimMapperImpl implements ClaimMapper {

    @Override
    public Claim toEntity(ClaimRequestDto requestDto) {

        if (requestDto == null) {
            return null;
        }

        return Claim.builder()
                .claimAmount(requestDto.getClaimAmount())
                .claimReason(requestDto.getClaimReason())
                .claimDate(requestDto.getClaimDate())
                .build();
    }

    @Override
    public ClaimResponseDto toResponseDto(Claim claim) {

        if (claim == null) {
            return null;
        }

        return ClaimResponseDto.builder()
                .claimId(claim.getId())
                .claimNumber(claim.getClaimNumber())
                .claimAmount(claim.getClaimAmount())
                .approvedAmount(claim.getApprovedAmount())
                .claimReason(claim.getClaimReason())
                .claimDate(claim.getClaimDate())
                .settlementDate(claim.getSettlementDate())
                .status(claim.getStatus())
                .policyId(claim.getPolicy().getId())
                .build();
    }
}