package com.insurance.backend.controller;

import com.insurance.backend.dto.request.ClaimApprovalRequestDto;
import com.insurance.backend.dto.request.ClaimRequestDto;
import com.insurance.backend.dto.response.ApiResponse;
import com.insurance.backend.dto.response.ClaimResponseDto;
import com.insurance.backend.service.ClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClaimResponseDto>> createClaim(
            @Valid @RequestBody ClaimRequestDto requestDto) {

        ClaimResponseDto response = claimService.createClaim(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Claim created successfully.", response));
    }

    @PutMapping("/{claimId}")
    public ResponseEntity<ApiResponse<ClaimResponseDto>> updateClaim(
            @PathVariable Long claimId,
            @Valid @RequestBody ClaimRequestDto requestDto) {

        ClaimResponseDto response = claimService.updateClaim(claimId, requestDto);

        return ResponseEntity.ok(
                ApiResponse.success("Claim updated successfully.", response)
        );
    }

    @DeleteMapping("/{claimId}")
    public ResponseEntity<ApiResponse<String>> deleteClaim(
            @PathVariable Long claimId) {

        claimService.deleteClaim(claimId);

        return ResponseEntity.ok(
                ApiResponse.success("Claim deleted successfully.", null)
        );
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<ApiResponse<ClaimResponseDto>> getClaimById(
            @PathVariable Long claimId) {

        ClaimResponseDto response = claimService.getClaimById(claimId);

        return ResponseEntity.ok(
                ApiResponse.success("Claim retrieved successfully.", response)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClaimResponseDto>>> getAllClaims() {

        List<ClaimResponseDto> response = claimService.getAllClaims();

        return ResponseEntity.ok(
                ApiResponse.success("Claims retrieved successfully.", response)
        );
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<ClaimResponseDto>>> getClaimsByPolicyId(
            @PathVariable Long policyId) {

        List<ClaimResponseDto> response = claimService.getClaimsByPolicyId(policyId);

        return ResponseEntity.ok(
                ApiResponse.success("Claims retrieved successfully.", response)
        );
    }

    @PutMapping("/{claimId}/approve")
    public ResponseEntity<ApiResponse<ClaimResponseDto>> approveClaim(
            @PathVariable Long claimId,
            @Valid @RequestBody ClaimApprovalRequestDto requestDto) {

        ClaimResponseDto response =
                claimService.approveClaim(claimId, requestDto);

        return ResponseEntity.ok(
                ApiResponse.success("Claim approved successfully.", response)
        );
    }
}