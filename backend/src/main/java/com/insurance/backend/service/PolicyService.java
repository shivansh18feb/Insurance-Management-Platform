package com.insurance.backend.service;

import com.insurance.backend.dto.request.PolicyRequestDto;
import com.insurance.backend.dto.response.PolicyResponseDto;

import java.util.List;

public interface PolicyService {

    PolicyResponseDto createPolicy(PolicyRequestDto requestDto);

    PolicyResponseDto updatePolicy(Long policyId, PolicyRequestDto requestDto);

    void deletePolicy(Long policyId);

    PolicyResponseDto getPolicyById(Long policyId);

    List<PolicyResponseDto> getAllPolicies();

    List<PolicyResponseDto> getPoliciesByCustomer(Long customerId);

}