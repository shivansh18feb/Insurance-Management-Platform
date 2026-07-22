package com.insurance.backend.service;

import com.insurance.backend.dto.request.CustomerRequestDto;
import com.insurance.backend.dto.response.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    CustomerResponseDto createCustomer(CustomerRequestDto requestDto);

    CustomerResponseDto getCustomerById(Long id);

    List<CustomerResponseDto> getAllCustomers();

    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto requestDto);

    void deleteCustomer(Long id);
}