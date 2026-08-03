package com.insurance.backend.service.impl;

import com.insurance.backend.dto.response.DocumentResponseDto;
import com.insurance.backend.entity.Claim;
import com.insurance.backend.entity.Customer;
import com.insurance.backend.entity.Document;
import com.insurance.backend.entity.Policy;
import com.insurance.backend.entity.enums.DocumentType;
import com.insurance.backend.exception.InvalidRequestException;
import com.insurance.backend.exception.ResourceNotFoundException;
import com.insurance.backend.repository.ClaimRepository;
import com.insurance.backend.repository.CustomerRepository;
import com.insurance.backend.repository.DocumentRepository;
import com.insurance.backend.repository.PolicyRepository;
import com.insurance.backend.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final CustomerRepository customerRepository;
    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Override
    public DocumentResponseDto uploadDocument(MultipartFile file, Long customerId, Long policyId, Long claimId, DocumentType documentType, String description) {
        if (file.isEmpty()) {
            throw new InvalidRequestException("Failed to store empty file");
        }

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new InvalidRequestException("Could not create directory for upload");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Policy policy = policyId != null ? policyRepository.findById(policyId).orElse(null) : null;
        Claim claim = claimId != null ? claimRepository.findById(claimId).orElse(null) : null;

        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String storedFileName = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = this.fileStorageLocation.resolve(storedFileName);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new InvalidRequestException("Could not store file " + originalFileName);
        }

        Document document = Document.builder()
                .fileName(storedFileName)
                .originalFileName(originalFileName)
                .filePath(targetLocation.toString())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .documentType(documentType != null ? documentType : DocumentType.OTHER)
                .description(description)
                .customer(customer)
                .policy(policy)
                .claim(claim)
                .uploadedAt(LocalDateTime.now())
                .verified(false)
                .build();

        return mapToDto(documentRepository.save(document));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponseDto> getCustomerDocuments(Long customerId) {
        return documentRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponseDto> getPolicyDocuments(Long policyId) {
        return documentRepository.findByPolicyId(policyId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponseDto> getClaimDocuments(Long claimId) {
        return documentRepository.findByClaimId(claimId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Resource loadDocumentAsResource(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));

        try {
            Path filePath = Paths.get(document.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found on server");
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File path invalid");
        }
    }

    @Override
    public DocumentResponseDto verifyDocument(Long documentId, String verifiedBy) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));

        document.setVerified(true);
        document.setVerifiedBy(verifiedBy);
        return mapToDto(documentRepository.save(document));
    }

    @Override
    public void deleteDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));

        try {
            Path filePath = Paths.get(document.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {}

        documentRepository.delete(document);
    }

    private DocumentResponseDto mapToDto(Document doc) {
        return DocumentResponseDto.builder()
                .id(doc.getId())
                .fileName(doc.getFileName())
                .originalFileName(doc.getOriginalFileName())
                .fileType(doc.getFileType())
                .fileSize(doc.getFileSize())
                .documentType(doc.getDocumentType())
                .description(doc.getDescription())
                .customerId(doc.getCustomer() != null ? doc.getCustomer().getId() : null)
                .policyId(doc.getPolicy() != null ? doc.getPolicy().getId() : null)
                .claimId(doc.getClaim() != null ? doc.getClaim().getId() : null)
                .uploadedAt(doc.getUploadedAt())
                .verified(doc.isVerified())
                .verifiedBy(doc.getVerifiedBy())
                .build();
    }
}
