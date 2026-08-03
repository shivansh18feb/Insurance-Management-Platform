package com.insurance.backend.service.impl;

import com.insurance.backend.constant.MessageConstants;
import com.insurance.backend.dto.request.PolicyRequestDto;
import com.insurance.backend.dto.response.PolicyResponseDto;
import com.insurance.backend.entity.Customer;
import com.insurance.backend.entity.Policy;
import com.insurance.backend.entity.enums.PolicyStatus;
import com.insurance.backend.exception.InvalidRequestException;
import com.insurance.backend.exception.ResourceNotFoundException;
import com.insurance.backend.mapper.PolicyMapper;
import com.insurance.backend.repository.CustomerRepository;
import com.insurance.backend.repository.PolicyRepository;
import com.insurance.backend.service.PolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final PolicyMapper policyMapper;

    @Override
    @Transactional
    public PolicyResponseDto createPolicy(PolicyRequestDto requestDto) {

        normalizeRequest(requestDto);
        validatePolicyDates(requestDto.getStartDate(), requestDto.getEndDate());

        Customer customer = getCustomerEntity(requestDto.getCustomerId());

        Policy policy = new Policy();

        policy.setPolicyName(requestDto.getPolicyName());
        policy.setPolicyType(requestDto.getPolicyType());
        policy.setCoverageAmount(requestDto.getCoverageAmount());
        policy.setPremiumAmount(requestDto.getPremiumAmount());
        policy.setStartDate(requestDto.getStartDate());
        policy.setEndDate(requestDto.getEndDate());

        policy.setCustomer(customer);
        policy.setPolicyNumber(generatePolicyNumber());
        policy.setStatus(PolicyStatus.ACTIVE);

        Policy savedPolicy = policyRepository.save(policy);

        log.info("Policy created successfully. id={}, policyNumber={}",
                savedPolicy.getId(), savedPolicy.getPolicyNumber());

        return policyMapper.toResponseDto(savedPolicy);
    }

    @Override
    @Transactional
    public PolicyResponseDto updatePolicy(Long policyId, PolicyRequestDto requestDto) {

        Policy policy = getPolicyEntity(policyId);

        normalizeRequest(requestDto);
        validatePolicyDates(requestDto.getStartDate(), requestDto.getEndDate());

        Customer customer = getCustomerEntity(requestDto.getCustomerId());

        policyMapper.updateEntity(requestDto, policy);
        policy.setCustomer(customer);

        Policy updatedPolicy = policyRepository.save(policy);

        log.info("Policy updated successfully. id={}, policyNumber={}",
                updatedPolicy.getId(), updatedPolicy.getPolicyNumber());

        return policyMapper.toResponseDto(updatedPolicy);
    }

    @Override
    @Transactional
    public void deletePolicy(Long policyId) {

        Policy policy = getPolicyEntity(policyId);

        policyRepository.delete(policy);

        log.info("Policy deleted successfully. id={}, policyNumber={}",
                policy.getId(), policy.getPolicyNumber());
    }

    @Override
    public PolicyResponseDto getPolicyById(Long policyId) {
        return policyMapper.toResponseDto(getPolicyEntity(policyId));
    }

    @Override
    public List<PolicyResponseDto> getAllPolicies() {

        return policyRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(policyMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<PolicyResponseDto> getPoliciesByCustomer(Long customerId) {

        getCustomerEntity(customerId);

        return policyRepository.findByCustomerId(customerId)
                .stream()
                .map(policyMapper::toResponseDto)
                .toList();
    }

    private Policy getPolicyEntity(Long policyId) {

        return policyRepository.findById(policyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                MessageConstants.POLICY_NOT_FOUND + policyId));
    }

    private Customer getCustomerEntity(Long customerId) {

        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                MessageConstants.CUSTOMER_NOT_FOUND + customerId));
    }

    private void validatePolicyDates(LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new InvalidRequestException(
                    MessageConstants.INVALID_POLICY_DATE);
        }
    }

    private void normalizeRequest(PolicyRequestDto requestDto) {
        requestDto.setPolicyName(normalize(requestDto.getPolicyName()));
    }

    private String normalize(String value) {
        return value == null ? null : value.strip();
    }

    private String generatePolicyNumber() {

        int currentYear = Year.now().getValue();
        int nextSequence = 1;

        Optional<Policy> latestPolicy = policyRepository.findTopByOrderByIdDesc();

        if (latestPolicy.isPresent()) {

            String policyNumber = latestPolicy.get().getPolicyNumber();

            try {
                String[] parts = policyNumber.split("-");
                int year = Integer.parseInt(parts[1]);
                int sequence = Integer.parseInt(parts[2]);

                if (year == currentYear) {
                    nextSequence = sequence + 1;
                }

            } catch (Exception exception) {
                log.warn("Invalid policy number format: {}", policyNumber);
            }
        }

        return String.format("POL-%d-%06d", currentYear, nextSequence);
    }
}
