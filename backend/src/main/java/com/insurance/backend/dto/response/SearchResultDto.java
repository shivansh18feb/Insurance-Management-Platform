package com.insurance.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto {
    private List<CustomerResponseDto> customers;
    private List<PolicyResponseDto> policies;
    private List<ClaimResponseDto> claims;
}
