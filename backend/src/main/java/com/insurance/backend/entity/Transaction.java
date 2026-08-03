package com.insurance.backend.entity;

import com.insurance.backend.entity.enums.PaymentMethod;
import com.insurance.backend.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions",
        indexes = {
                @Index(name = "idx_transaction_number", columnList = "transactionNumber"),
                @Index(name = "idx_transaction_type", columnList = "transactionType"),
                @Index(name = "idx_transaction_user", columnList = "user_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String transactionNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionType transactionType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "fk_transaction_user"))
    private User initiatedBy;

    // Optional policy / claim / premium references
    @Column
    private Long policyId;

    @Column
    private Long claimId;

    @Column
    private Long premiumScheduleId;
}
