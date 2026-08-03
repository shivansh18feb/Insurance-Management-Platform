package com.insurance.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.insurance.backend.entity.enums.PolicyStatus;
import com.insurance.backend.entity.enums.PolicyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "policies",
        indexes = {
                @Index(name = "idx_policy_number", columnList = "policyNumber"),
                @Index(name = "idx_policy_status", columnList = "status"),
                @Index(name = "idx_policy_type", columnList = "policyType"),
                @Index(name = "idx_policy_customer", columnList = "customer_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String policyNumber;

    @NotBlank(message = "Policy name is required")
    @Column(nullable = false, length = 100)
    private String policyName;

    @NotNull(message = "Policy type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PolicyType policyType;

    @NotNull(message = "Coverage amount is required")
    @DecimalMin(value = "0.01", message = "Coverage amount must be greater than zero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal coverageAmount;

    @NotNull(message = "Premium amount is required")
    @DecimalMin(value = "0.01", message = "Premium amount must be greater than zero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal premiumAmount;

    @NotNull(message = "Policy start date is required")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "Policy end date is required")
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull(message = "Policy status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PolicyStatus status = PolicyStatus.PENDING;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "customer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_policy_customer")
    )
    private Customer customer;
}