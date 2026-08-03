package com.insurance.backend.repository;

import com.insurance.backend.entity.AgentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentProfileRepository extends JpaRepository<AgentProfile, Long> {
    Optional<AgentProfile> findByUserId(Long userId);
    Optional<AgentProfile> findByAgentCode(String agentCode);
    boolean existsByAgentCode(String agentCode);
}
