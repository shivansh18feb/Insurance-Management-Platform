package com.insurance.backend.service.impl;

import com.insurance.backend.constant.MessageConstants;
import com.insurance.backend.dto.request.CustomerRequestDto;
import com.insurance.backend.dto.response.CustomerResponseDto;
import com.insurance.backend.entity.Customer;
import com.insurance.backend.entity.CustomerStatus;
import com.insurance.backend.exception.DuplicateResourceException;
import com.insurance.backend.exception.ResourceNotFoundException;
import com.insurance.backend.mapper.CustomerMapper;
import com.insurance.backend.repository.CustomerRepository;
import com.insurance.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto requestDto) {

        normalizeRequest(requestDto);

        validateDuplicateEmail(requestDto.getEmail());
        validateDuplicatePhone(requestDto.getPhoneNumber());

        Customer customer = customerMapper.toEntity(requestDto);
        customer.setStatus(CustomerStatus.ACTIVE);

        Customer savedCustomer = customerRepository.save(customer);

        log.info(
                "Customer created successfully. id={}, email={}",
                savedCustomer.getId(),
                savedCustomer.getEmail());

        return customerMapper.toResponseDto(savedCustomer);
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {

        Customer customer = getCustomerEntity(id);

        return customerMapper.toResponseDto(customer);
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {

        return customerRepository.findAll(Sort.by("firstName"))
                .stream()
                .map(customerMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(
            Long id,
            CustomerRequestDto requestDto) {

        Customer customer = getCustomerEntity(id);

        normalizeRequest(requestDto);

        validateUniqueFields(customer, requestDto);

        customerMapper.updateEntity(requestDto, customer);

        Customer updatedCustomer = customerRepository.save(customer);

        log.info(
                "Customer updated successfully. id={}, email={}",
                updatedCustomer.getId(),
                updatedCustomer.getEmail());

        return customerMapper.toResponseDto(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {

        Customer customer = getCustomerEntity(id);

        customerRepository.delete(customer);

        log.info(
                "Customer deleted successfully. id={}, email={}",
                customer.getId(),
                customer.getEmail());
    }

    private Customer getCustomerEntity(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                MessageConstants.CUSTOMER_NOT_FOUND + id));
    }

    private void validateUniqueFields(
            Customer customer,
            CustomerRequestDto requestDto) {

        if (!customer.getEmail().equals(requestDto.getEmail())) {
            validateDuplicateEmail(requestDto.getEmail());
        }

        if (!customer.getPhoneNumber().equals(requestDto.getPhoneNumber())) {
            validateDuplicatePhone(requestDto.getPhoneNumber());
        }
    }

    private void validateDuplicateEmail(String email) {

        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    MessageConstants.EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateDuplicatePhone(String phoneNumber) {

        if (customerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicateResourceException(
                    MessageConstants.PHONE_ALREADY_EXISTS);
        }
    }

    private void normalizeRequest(CustomerRequestDto requestDto) {

        requestDto.setFirstName(normalize(requestDto.getFirstName()));
        requestDto.setLastName(normalize(requestDto.getLastName()));
        requestDto.setEmail(normalizeEmail(requestDto.getEmail()));
        requestDto.setPhoneNumber(normalize(requestDto.getPhoneNumber()));
        requestDto.setAddress(normalize(requestDto.getAddress()));
        requestDto.setCity(normalize(requestDto.getCity()));
        requestDto.setState(normalize(requestDto.getState()));
        requestDto.setPostalCode(normalize(requestDto.getPostalCode()));
    }

    private String normalize(String value) {

        return value == null ? null : value.strip();
    }

    private String normalizeEmail(String email) {

        return email == null
                ? null
                : email.strip().toLowerCase();
    }
}