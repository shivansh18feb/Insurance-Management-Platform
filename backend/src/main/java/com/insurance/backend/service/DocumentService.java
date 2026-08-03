package com.insurance.backend.service;

import com.insurance.backend.dto.response.DocumentResponseDto;
import com.insurance.backend.entity.enums.DocumentType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    DocumentResponseDto uploadDocument(MultipartFile file, Long customerId, Long policyId, Long claimId, DocumentType documentType, String description);
    List<DocumentResponseDto> getCustomerDocuments(Long customerId);
    List<DocumentResponseDto> getPolicyDocuments(Long policyId);
    List<DocumentResponseDto> getClaimDocuments(Long claimId);
    Resource loadDocumentAsResource(Long documentId);
    DocumentResponseDto verifyDocument(Long documentId, String verifiedBy);
    void deleteDocument(Long documentId);
}
