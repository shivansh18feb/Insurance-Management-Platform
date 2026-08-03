package com.insurance.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee_profiles",
        indexes = {
                @Index(name = "idx_employee_code", columnList = "employeeCode")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_employee_user"))
    private User user;

    @Column(nullable = false, unique = true, length = 30)
    private String employeeCode;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String position;

    @Column
    private LocalDate hireDate;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
