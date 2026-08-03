package com.insurance.backend.mapper.impl;

import com.insurance.backend.dto.request.PolicyRequestDto;
import com.insurance.backend.dto.response.PolicyResponseDto;
import com.insurance.backend.entity.Customer;
import com.insurance.backend.entity.Policy;
import com.insurance.backend.mapper.PolicyMapper;
import org.springframework.stereotype.Component;

@Component
public class PolicyMapperImpl implements PolicyMapper {

    @Override
    public Policy toEntity(PolicyRequestDto requestDto) {

        if (requestDto == null) {
            return null;
        }

        Policy policy = new Policy();

        policy.setPolicyName(requestDto.getPolicyName());
        policy.setPolicyType(requestDto.getPolicyType());
        policy.setCoverageAmount(requestDto.getCoverageAmount());
        policy.setPremiumAmount(requestDto.getPremiumAmount());
        policy.setStartDate(requestDto.getStartDate());
        policy.setEndDate(requestDto.getEndDate());

        return policy;
    }

    @Override
    public PolicyResponseDto toResponseDto(Policy policy) {

        if (policy == null) {
            return null;
        }

        PolicyResponseDto response = new PolicyResponseDto();

        response.setId(policy.getId());
        response.setPolicyNumber(policy.getPolicyNumber());
        response.setPolicyName(policy.getPolicyName());
        response.setPolicyType(policy.getPolicyType());
        response.setCoverageAmount(policy.getCoverageAmount());
        response.setPremiumAmount(policy.getPremiumAmount());
        response.setStartDate(policy.getStartDate());
        response.setEndDate(policy.getEndDate());
        response.setStatus(policy.getStatus());
        response.setCreatedAt(policy.getCreatedAt());
        response.setUpdatedAt(policy.getUpdatedAt());

        Customer customer = policy.getCustomer();
        if (customer != null) {
            response.setCustomerId(customer.getId());

            String fullName = String.format("%s %s",
                    customer.getFirstName(),
                    customer.getLastName());

            response.setCustomerFullName(fullName);
        }

        return response;
    }

    @Override
    public void updateEntity(PolicyRequestDto requestDto, Policy policy) {

        if (requestDto == null || policy == null) {
            return;
        }

        policy.setPolicyName(requestDto.getPolicyName());
        policy.setPolicyType(requestDto.getPolicyType());
        policy.setCoverageAmount(requestDto.getCoverageAmount());
        policy.setPremiumAmount(requestDto.getPremiumAmount());
        policy.setStartDate(requestDto.getStartDate());
        policy.setEndDate(requestDto.getEndDate());
    }
}