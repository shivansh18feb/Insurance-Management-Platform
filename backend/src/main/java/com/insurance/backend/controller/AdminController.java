package com.insurance.backend.controller;

import com.insurance.backend.dto.request.AgentRequestDto;
import com.insurance.backend.dto.request.EmployeeRequestDto;
import com.insurance.backend.dto.response.*;
import com.insurance.backend.entity.enums.Role;
import com.insurance.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardDto>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.<AdminDashboardDto>builder()
                .success(true)
                .message("Dashboard data fetched successfully")
                .data(adminService.getDashboardStats())
                .build());
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.<List<UserResponseDto>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(adminService.getAllUsers())
                .build());
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUserRole(
            @PathVariable Long userId,
            @RequestParam Role role) {
        return ResponseEntity.ok(ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User role updated successfully")
                .data(adminService.updateUserRole(userId, role))
                .build());
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<UserResponseDto>> toggleUserStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User status toggled successfully")
                .data(adminService.toggleUserStatus(userId))
                .build());
    }

    @PostMapping("/agents")
    public ResponseEntity<ApiResponse<AgentResponseDto>> createAgent(
            @Valid @RequestBody AgentRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AgentResponseDto>builder()
                        .success(true)
                        .message("Agent created successfully")
                        .data(adminService.createAgent(request))
                        .build());
    }

    @GetMapping("/agents")
    public ResponseEntity<ApiResponse<List<AgentResponseDto>>> getAllAgents() {
        return ResponseEntity.ok(ApiResponse.<List<AgentResponseDto>>builder()
                .success(true)
                .message("Agents fetched successfully")
                .data(adminService.getAllAgents())
                .build());
    }

    @PostMapping("/employees")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> createEmployee(
            @Valid @RequestBody EmployeeRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<EmployeeResponseDto>builder()
                        .success(true)
                        .message("Employee created successfully")
                        .data(adminService.createEmployee(request))
                        .build());
    }

    @GetMapping("/employees")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getAllEmployees() {
        return ResponseEntity.ok(ApiResponse.<List<EmployeeResponseDto>>builder()
                .success(true)
                .message("Employees fetched successfully")
                .data(adminService.getAllEmployees())
                .build());
    }
}
