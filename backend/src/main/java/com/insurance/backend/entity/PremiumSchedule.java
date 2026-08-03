package com.insurance.backend.entity;

import com.insurance.backend.entity.enums.PremiumStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "premium_schedules",
        indexes = {
                @Index(name = "idx_premium_policy", columnList = "policy_id"),
                @Index(name = "idx_premium_status", columnList = "status"),
                @Index(name = "idx_premium_due_date", columnList = "dueDate")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PremiumSchedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_premium_policy"))
    private Policy policy;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PremiumStatus status = PremiumStatus.PENDING;

    @Column
    private LocalDate paidDate;

    @Column
    private Integer installmentNumber;

    @Column(length = 255)
    private String remarks;

    @Column(unique = true, length = 50)
    private String premiumNumber;
}
