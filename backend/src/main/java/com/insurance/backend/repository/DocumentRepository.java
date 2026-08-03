package com.insurance.backend.repository;

import com.insurance.backend.entity.Document;
import com.insurance.backend.entity.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCustomerId(Long customerId);
    List<Document> findByPolicyId(Long policyId);
    List<Document> findByClaimId(Long claimId);
    List<Document> findByCustomerIdAndDocumentType(Long customerId, DocumentType documentType);
}
