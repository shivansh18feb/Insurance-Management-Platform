package com.insurance.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
    private Long id;
    private Long userId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String position;
    private LocalDate hireDate;
    private String phoneNumber;
    private boolean active;
    private LocalDateTime createdAt;
}
