package com.insurance.backend.controller;

import com.insurance.backend.dto.response.ApiResponse;
import com.insurance.backend.dto.response.DocumentResponseDto;
import com.insurance.backend.entity.enums.DocumentType;
import com.insurance.backend.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DocumentResponseDto>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("customerId") Long customerId,
            @RequestParam(value = "policyId", required = false) Long policyId,
            @RequestParam(value = "claimId", required = false) Long claimId,
            @RequestParam(value = "documentType", required = false) DocumentType documentType,
            @RequestParam(value = "description", required = false) String description) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<DocumentResponseDto>builder()
                        .success(true)
                        .message("Document uploaded successfully")
                        .data(documentService.uploadDocument(file, customerId, policyId, claimId, documentType, description))
                        .build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getCustomerDocuments(@PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.<List<DocumentResponseDto>>builder()
                .success(true)
                .message("Customer documents fetched")
                .data(documentService.getCustomerDocuments(customerId))
                .build());
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getPolicyDocuments(@PathVariable Long policyId) {
        return ResponseEntity.ok(ApiResponse.<List<DocumentResponseDto>>builder()
                .success(true)
                .message("Policy documents fetched")
                .data(documentService.getPolicyDocuments(policyId))
                .build());
    }

    @GetMapping("/claim/{claimId}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getClaimDocuments(@PathVariable Long claimId) {
        return ResponseEntity.ok(ApiResponse.<List<DocumentResponseDto>>builder()
                .success(true)
                .message("Claim documents fetched")
                .data(documentService.getClaimDocuments(claimId))
                .build());
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        Resource resource = documentService.loadDocumentAsResource(documentId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping("/{documentId}/verify")
    public ResponseEntity<ApiResponse<DocumentResponseDto>> verifyDocument(
            @PathVariable Long documentId,
            Authentication authentication) {
        String verifier = authentication != null ? authentication.getName() : "AGENT";
        return ResponseEntity.ok(ApiResponse.<DocumentResponseDto>builder()
                .success(true)
                .message("Document verified successfully")
                .data(documentService.verifyDocument(documentId, verifier))
                .build());
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<ApiResponse<String>> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Document deleted successfully")
                .data(null)
                .build());
    }
}
