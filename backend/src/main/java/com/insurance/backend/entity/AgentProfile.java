package com.insurance.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agent_profiles",
        indexes = {
                @Index(name = "idx_agent_code", columnList = "agentCode"),
                @Index(name = "idx_agent_license", columnList = "licenseNumber")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_agent_user"))
    private User user;

    @Column(nullable = false, unique = true, length = 30)
    private String agentCode;

    @Column(unique = true, length = 50)
    private String licenseNumber;

    @Column(length = 100)
    private String specialization;

    @Column(length = 100)
    private String assignedRegion;

    @Column(precision = 5, scale = 2)
    private java.math.BigDecimal commissionRate;

    @Column(precision = 15, scale = 2)
    @Builder.Default
    private java.math.BigDecimal totalCommission = java.math.BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
