package com.insurance.backend.mapper;

import com.insurance.backend.dto.request.PolicyRequestDto;
import com.insurance.backend.dto.response.PolicyResponseDto;
import com.insurance.backend.entity.Policy;

public interface PolicyMapper {

    Policy toEntity(PolicyRequestDto requestDto);

    PolicyResponseDto toResponseDto(Policy policy);

    void updateEntity(PolicyRequestDto requestDto, Policy policy);
}