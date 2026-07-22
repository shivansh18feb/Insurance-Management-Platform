package com.insurance.backend.mapper;

import com.insurance.backend.dto.request.CustomerRequestDto;
import com.insurance.backend.dto.response.CustomerResponseDto;
import com.insurance.backend.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    private final ModelMapper modelMapper;

    public CustomerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Customer toEntity(CustomerRequestDto requestDto) {
        return modelMapper.map(requestDto, Customer.class);
    }

    public CustomerResponseDto toResponseDto(Customer customer) {
        return modelMapper.map(customer, CustomerResponseDto.class);
    }

    public void updateEntity(CustomerRequestDto requestDto, Customer customer) {
        modelMapper.map(requestDto, customer);
    }
}