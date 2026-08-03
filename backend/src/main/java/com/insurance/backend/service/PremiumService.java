package com.insurance.backend.service;

import com.insurance.backend.dto.response.PremiumScheduleResponseDto;

import java.util.List;

public interface PremiumService {
    List<PremiumScheduleResponseDto> generatePremiumSchedule(Long policyId, int numberOfInstallments);
    List<PremiumScheduleResponseDto> getPolicySchedule(Long policyId);
    PremiumScheduleResponseDto payPremium(Long premiumScheduleId, String paymentMethod, String remarks);
    List<PremiumScheduleResponseDto> getOverduePremiums();
}
