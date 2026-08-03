package com.insurance.backend.service.impl;

import com.insurance.backend.dto.request.AgentRequestDto;
import com.insurance.backend.dto.request.EmployeeRequestDto;
import com.insurance.backend.dto.response.AdminDashboardDto;
import com.insurance.backend.dto.response.AgentResponseDto;
import com.insurance.backend.dto.response.EmployeeResponseDto;
import com.insurance.backend.dto.response.UserResponseDto;
import com.insurance.backend.entity.*;
import com.insurance.backend.entity.enums.*;
import com.insurance.backend.exception.DuplicateResourceException;
import com.insurance.backend.exception.ResourceNotFoundException;
import com.insurance.backend.repository.*;
import com.insurance.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final PaymentRepository paymentRepository;
    private final AgentProfileRepository agentProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardDto getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalCustomers = customerRepository.count();
        long totalAgents = agentProfileRepository.count();
        long totalEmployees = employeeProfileRepository.count();
        long totalPolicies = policyRepository.count();
        
        List<Policy> allPolicies = policyRepository.findAll();
        long activePolicies = allPolicies.stream().filter(p -> p.getStatus() == PolicyStatus.ACTIVE).count();
        
        List<Claim> allClaims = claimRepository.findAll();
        long totalClaims = allClaims.size();
        long pendingClaims = allClaims.stream().filter(c -> c.getStatus() == ClaimStatus.SUBMITTED || c.getStatus() == ClaimStatus.UNDER_REVIEW).count();
        long approvedClaims = allClaims.stream().filter(c -> c.getStatus() == ClaimStatus.APPROVED).count();
        long settledClaims = allClaims.stream().filter(c -> c.getStatus() == ClaimStatus.SETTLED).count();

        List<Payment> payments = paymentRepository.findAll();
        BigDecimal totalRevenue = allPolicies.stream()
                .map(Policy::getPremiumAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalClaimPayout = payments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .map(Payment::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> policiesByType = new HashMap<>();
        for (PolicyType type : PolicyType.values()) {
            policiesByType.put(type.name(), allPolicies.stream().filter(p -> p.getPolicyType() == type).count());
        }

        Map<String, Long> claimsByStatus = new HashMap<>();
        for (ClaimStatus status : ClaimStatus.values()) {
            claimsByStatus.put(status.name(), allClaims.stream().filter(c -> c.getStatus() == status).count());
        }

        return AdminDashboardDto.builder()
                .totalUsers(totalUsers)
                .totalCustomers(totalCustomers)
                .totalAgents(totalAgents)
                .totalEmployees(totalEmployees)
                .totalPolicies(totalPolicies)
                .activePolicies(activePolicies)
                .totalClaims(totalClaims)
                .pendingClaims(pendingClaims)
                .approvedClaims(approvedClaims)
                .settledClaims(settledClaims)
                .totalRevenue(totalRevenue)
                .totalClaimPayout(totalClaimPayout)
                .policiesByType(policiesByType)
                .claimsByStatus(claimsByStatus)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUserRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(role);
        return mapUserToDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEnabled(!user.isEnabled());
        return mapUserToDto(userRepository.save(user));
    }

    @Override
    public AgentResponseDto createAgent(AgentRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.AGENT)
                .enabled(true)
                .build();
        userRepository.save(user);

        AgentProfile profile = AgentProfile.builder()
                .user(user)
                .agentCode("AGT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .licenseNumber(request.getLicenseNumber())
                .specialization(request.getSpecialization())
                .assignedRegion(request.getAssignedRegion())
                .commissionRate(request.getCommissionRate() != null ? request.getCommissionRate() : BigDecimal.valueOf(5.0))
                .totalCommission(BigDecimal.ZERO)
                .active(true)
                .build();
        agentProfileRepository.save(profile);

        return mapAgentToDto(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentResponseDto> getAllAgents() {
        return agentProfileRepository.findAll().stream()
                .map(this::mapAgentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDto createEmployee(EmployeeRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();
        userRepository.save(user);

        EmployeeProfile profile = EmployeeProfile.builder()
                .user(user)
                .employeeCode("EMP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .department(request.getDepartment())
                .position(request.getPosition())
                .hireDate(request.getHireDate())
                .phoneNumber(request.getPhoneNumber())
                .active(true)
                .build();
        employeeProfileRepository.save(profile);

        return mapEmployeeToDto(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeProfileRepository.findAll().stream()
                .map(this::mapEmployeeToDto)
                .collect(Collectors.toList());
    }

    private UserResponseDto mapUserToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .accountLocked(user.isAccountLocked())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private AgentResponseDto mapAgentToDto(AgentProfile profile) {
        return AgentResponseDto.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .agentCode(profile.getAgentCode())
                .firstName(profile.getUser().getFirstName())
                .lastName(profile.getUser().getLastName())
                .email(profile.getUser().getEmail())
                .licenseNumber(profile.getLicenseNumber())
                .specialization(profile.getSpecialization())
                .assignedRegion(profile.getAssignedRegion())
                .commissionRate(profile.getCommissionRate())
                .totalCommission(profile.getTotalCommission())
                .active(profile.isActive())
                .createdAt(profile.getCreatedAt())
                .build();
    }

    private EmployeeResponseDto mapEmployeeToDto(EmployeeProfile profile) {
        return EmployeeResponseDto.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .employeeCode(profile.getEmployeeCode())
                .firstName(profile.getUser().getFirstName())
                .lastName(profile.getUser().getLastName())
                .email(profile.getUser().getEmail())
                .department(profile.getDepartment())
                .position(profile.getPosition())
                .hireDate(profile.getHireDate())
                .phoneNumber(profile.getPhoneNumber())
                .active(profile.isActive())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}
