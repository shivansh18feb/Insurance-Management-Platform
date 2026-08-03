package com.insurance.backend.mapper;

import com.insurance.backend.dto.request.ClaimRequestDto;
import com.insurance.backend.dto.response.ClaimResponseDto;
import com.insurance.backend.entity.Claim;

public interface ClaimMapper {

    Claim toEntity(ClaimRequestDto requestDto);

    ClaimResponseDto toResponseDto(Claim claim);

}