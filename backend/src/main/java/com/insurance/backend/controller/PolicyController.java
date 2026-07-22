package com.insurance.backend.controller;

import com.insurance.backend.dto.request.PolicyRequestDto;
import com.insurance.backend.dto.response.ApiResponse;
import com.insurance.backend.dto.response.PolicyResponseDto;
import com.insurance.backend.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<ApiResponse<PolicyResponseDto>> createPolicy(
            @Valid @RequestBody PolicyRequestDto requestDto) {

        PolicyResponseDto policy = policyService.createPolicy(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Policy created successfully.",
                        policy));
    }

    @PutMapping("/{policyId}")
    public ResponseEntity<ApiResponse<PolicyResponseDto>> updatePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody PolicyRequestDto requestDto) {

        PolicyResponseDto policy =
                policyService.updatePolicy(policyId, requestDto);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy updated successfully.",
                        policy));
    }

    @DeleteMapping("/{policyId}")
    public ResponseEntity<ApiResponse<Void>> deletePolicy(
            @PathVariable Long policyId) {

        policyService.deletePolicy(policyId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy deleted successfully.",
                        null));
    }

    @GetMapping("/{policyId}")
    public ResponseEntity<ApiResponse<PolicyResponseDto>> getPolicyById(
            @PathVariable Long policyId) {

        PolicyResponseDto policy =
                policyService.getPolicyById(policyId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy retrieved successfully.",
                        policy));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PolicyResponseDto>>> getAllPolicies() {

        List<PolicyResponseDto> policies =
                policyService.getAllPolicies();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policies retrieved successfully.",
                        policies));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<PolicyResponseDto>>> getPoliciesByCustomer(
            @PathVariable Long customerId) {

        List<PolicyResponseDto> policies =
                policyService.getPoliciesByCustomer(customerId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer policies retrieved successfully.",
                        policies));
    }
}