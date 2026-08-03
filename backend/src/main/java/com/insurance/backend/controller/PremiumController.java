package com.insurance.backend.controller;

import com.insurance.backend.dto.response.ApiResponse;
import com.insurance.backend.dto.response.PremiumScheduleResponseDto;
import com.insurance.backend.service.PremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/premiums")
@RequiredArgsConstructor
public class PremiumController {

    private final PremiumService premiumService;

    @PostMapping("/policy/{policyId}/generate")
    public ResponseEntity<ApiResponse<List<PremiumScheduleResponseDto>>> generateSchedule(
            @PathVariable Long policyId,
            @RequestParam(defaultValue = "12") int installments) {
        return ResponseEntity.ok(ApiResponse.<List<PremiumScheduleResponseDto>>builder()
                .success(true)
                .message("Premium schedule generated")
                .data(premiumService.generatePremiumSchedule(policyId, installments))
                .build());
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<PremiumScheduleResponseDto>>> getSchedule(@PathVariable Long policyId) {
        return ResponseEntity.ok(ApiResponse.<List<PremiumScheduleResponseDto>>builder()
                .success(true)
                .message("Premium schedule fetched")
                .data(premiumService.getPolicySchedule(policyId))
                .build());
    }

    @PostMapping("/{scheduleId}/pay")
    public ResponseEntity<ApiResponse<PremiumScheduleResponseDto>> payPremium(
            @PathVariable Long scheduleId,
            @RequestParam(defaultValue = "UPI") String paymentMethod,
            @RequestParam(required = false) String remarks) {
        return ResponseEntity.ok(ApiResponse.<PremiumScheduleResponseDto>builder()
                .success(true)
                .message("Premium paid successfully")
                .data(premiumService.payPremium(scheduleId, paymentMethod, remarks))
                .build());
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<PremiumScheduleResponseDto>>> getOverdue() {
        return ResponseEntity.ok(ApiResponse.<List<PremiumScheduleResponseDto>>builder()
                .success(true)
                .message("Overdue premiums fetched")
                .data(premiumService.getOverduePremiums())
                .build());
    }
}
