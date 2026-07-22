package com.insurance.backend.mapper.impl;

import com.insurance.backend.dto.request.PolicyRequestDto;
import com.insurance.backend.dto.response.PolicyResponseDto;
import com.insurance.backend.entity.Policy;
import com.insurance.backend.mapper.PolicyMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PolicyMapperImpl implements PolicyMapper {

    private final ModelMapper modelMapper;

    public PolicyMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Policy toEntity(PolicyRequestDto requestDto) {
        return modelMapper.map(requestDto, Policy.class);
    }

    @Override
    public PolicyResponseDto toResponseDto(Policy policy) {

        PolicyResponseDto response = modelMapper.map(policy, PolicyResponseDto.class);

        response.setCustomerId(policy.getCustomer().getId());

        response.setCustomerFullName(
                String.format("%s %s",
                        policy.getCustomer().getFirstName(),
                        policy.getCustomer().getLastName()
                ).trim()
        );

        return response;
    }

    @Override
    public void updateEntity(PolicyRequestDto requestDto, Policy policy) {
        modelMapper.map(requestDto, policy);
    }
}