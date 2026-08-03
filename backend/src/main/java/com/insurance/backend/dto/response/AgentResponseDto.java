package com.insurance.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponseDto {
    private Long id;
    private Long userId;
    private String agentCode;
    private String firstName;
    private String lastName;
    private String email;
    private String licenseNumber;
    private String specialization;
    private String assignedRegion;
    private BigDecimal commissionRate;
    private BigDecimal totalCommission;
    private boolean active;
    private LocalDateTime createdAt;
}
