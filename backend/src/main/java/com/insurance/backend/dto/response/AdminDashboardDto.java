package com.insurance.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDto {
    private long totalUsers;
    private long totalCustomers;
    private long totalAgents;
    private long totalEmployees;
    private long totalPolicies;
    private long activePolicies;
    private long totalClaims;
    private long pendingClaims;
    private long approvedClaims;
    private long settledClaims;
    private BigDecimal totalRevenue;
    private BigDecimal totalClaimPayout;
    private Map<String, Long> policiesByType;
    private Map<String, Long> claimsByStatus;
}
