package com.insurance.backend.dto.response;

import com.insurance.backend.entity.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDto {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String fileType;
    private Long fileSize;
    private DocumentType documentType;
    private String description;
    private Long customerId;
    private Long policyId;
    private Long claimId;
    private LocalDateTime uploadedAt;
    private boolean verified;
    private String verifiedBy;
}
