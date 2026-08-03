package com.insurance.backend.service.impl;

import com.insurance.backend.dto.response.ClaimResponseDto;
import com.insurance.backend.dto.response.CustomerResponseDto;
import com.insurance.backend.dto.response.PolicyResponseDto;
import com.insurance.backend.dto.response.SearchResultDto;
import com.insurance.backend.mapper.ClaimMapper;
import com.insurance.backend.mapper.CustomerMapper;
import com.insurance.backend.mapper.PolicyMapper;
import com.insurance.backend.repository.ClaimRepository;
import com.insurance.backend.repository.CustomerRepository;
import com.insurance.backend.repository.PolicyRepository;
import com.insurance.backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final CustomerRepository customerRepository;
    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;

    private final CustomerMapper customerMapper;
    private final PolicyMapper policyMapper;
    private final ClaimMapper claimMapper;

    @Override
    public SearchResultDto globalSearch(String query) {
        if (query == null || query.isBlank()) {
            return SearchResultDto.builder()
                    .customers(List.of())
                    .policies(List.of())
                    .claims(List.of())
                    .build();
        }

        String q = query.toLowerCase().trim();

        List<CustomerResponseDto> customers = customerRepository.findAll().stream()
                .filter(c -> c.getFirstName().toLowerCase().contains(q) ||
                             c.getLastName().toLowerCase().contains(q) ||
                             c.getEmail().toLowerCase().contains(q) ||
                             c.getPhoneNumber().contains(q))
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());

        List<PolicyResponseDto> policies = policyRepository.findAll().stream()
                .filter(p -> p.getPolicyNumber().toLowerCase().contains(q) ||
                             p.getPolicyName().toLowerCase().contains(q))
                .map(policyMapper::toResponseDto)
                .collect(Collectors.toList());

        List<ClaimResponseDto> claims = claimRepository.findAll().stream()
                .filter(c -> c.getClaimNumber().toLowerCase().contains(q) ||
                             c.getClaimReason().toLowerCase().contains(q))
                .map(claimMapper::toResponseDto)
                .collect(Collectors.toList());

        return SearchResultDto.builder()
                .customers(customers)
                .policies(policies)
                .claims(claims)
                .build();
    }
}
