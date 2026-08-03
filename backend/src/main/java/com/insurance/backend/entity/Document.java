package com.insurance.backend.entity;

import com.insurance.backend.entity.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents",
        indexes = {
                @Index(name = "idx_document_customer", columnList = "customer_id"),
                @Index(name = "idx_document_type", columnList = "documentType")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 255)
    private String originalFileName;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false, length = 100)
    private String fileType;

    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocumentType documentType;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_document_customer"))
    private Customer customer;

    // Optional: link to specific policy or claim
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id",
            foreignKey = @ForeignKey(name = "fk_document_policy"))
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id",
            foreignKey = @ForeignKey(name = "fk_document_claim"))
    private Claim claim;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean verified = false;

    @Column(length = 255)
    private String verifiedBy;
}
