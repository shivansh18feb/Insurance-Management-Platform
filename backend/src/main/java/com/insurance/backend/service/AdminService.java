package com.insurance.backend.service;

import com.insurance.backend.dto.request.AgentRequestDto;
import com.insurance.backend.dto.request.EmployeeRequestDto;
import com.insurance.backend.dto.response.AdminDashboardDto;
import com.insurance.backend.dto.response.AgentResponseDto;
import com.insurance.backend.dto.response.EmployeeResponseDto;
import com.insurance.backend.dto.response.UserResponseDto;
import com.insurance.backend.entity.enums.Role;

import java.util.List;

public interface AdminService {
    AdminDashboardDto getDashboardStats();
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUserRole(Long userId, Role role);
    UserResponseDto toggleUserStatus(Long userId);
    
    AgentResponseDto createAgent(AgentRequestDto request);
    List<AgentResponseDto> getAllAgents();
    
    EmployeeResponseDto createEmployee(EmployeeRequestDto request);
    List<EmployeeResponseDto> getAllEmployees();
}
