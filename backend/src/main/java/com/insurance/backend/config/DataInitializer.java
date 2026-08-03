package com.insurance.backend.config;

import com.insurance.backend.entity.*;
import com.insurance.backend.entity.enums.*;
import com.insurance.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AgentProfileRepository agentProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser();
        seedAgentUser();
        seedEmployeeUser();
        seedCustomerUser();
    }

    private void seedAdminUser() {
        if (!userRepository.existsByEmail("admin@insurance.com")) {
            User admin = User.builder()
                    .firstName("System")
                    .lastName("Administrator")
                    .email("admin@insurance.com")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .accountLocked(false)
                    .failedLoginAttempts(0)
                    .build();
            userRepository.save(admin);
            log.info("Default Admin account seeded: admin@insurance.com / Admin@1234");
        }
    }

    private void seedAgentUser() {
        if (!userRepository.existsByEmail("agent@insurance.com")) {
            User agentUser = User.builder()
                    .firstName("Arthur")
                    .lastName("Agent")
                    .email("agent@insurance.com")
                    .password(passwordEncoder.encode("Agent@1234"))
                    .role(Role.AGENT)
                    .enabled(true)
                    .accountLocked(false)
                    .failedLoginAttempts(0)
                    .build();
            userRepository.save(agentUser);

            AgentProfile profile = AgentProfile.builder()
                    .user(agentUser)
                    .agentCode("AGT-PRIMARY")
                    .licenseNumber("LIC-998877")
                    .specialization("LIFE & HEALTH")
                    .assignedRegion("Mumbai Central")
                    .commissionRate(BigDecimal.valueOf(7.5))
                    .totalCommission(BigDecimal.ZERO)
                    .active(true)
                    .build();
            agentProfileRepository.save(profile);
            log.info("Default Agent account seeded: agent@insurance.com / Agent@1234");
        }
    }

    private void seedEmployeeUser() {
        if (!userRepository.existsByEmail("employee@insurance.com")) {
            User empUser = User.builder()
                    .firstName("Edward")
                    .lastName("Employee")
                    .email("employee@insurance.com")
                    .password(passwordEncoder.encode("Employee@1234"))
                    .role(Role.EMPLOYEE)
                    .enabled(true)
                    .accountLocked(false)
                    .failedLoginAttempts(0)
                    .build();
            userRepository.save(empUser);

            EmployeeProfile profile = EmployeeProfile.builder()
                    .user(empUser)
                    .employeeCode("EMP-PRIMARY")
                    .department("Claims")
                    .position("Senior Claims Evaluator")
                    .hireDate(LocalDate.of(2023, 1, 15))
                    .phoneNumber("9876543210")
                    .active(true)
                    .build();
            employeeProfileRepository.save(profile);
            log.info("Default Employee account seeded: employee@insurance.com / Employee@1234");
        }
    }

    private void seedCustomerUser() {
        if (!userRepository.existsByEmail("customer@insurance.com")) {
            User customerUser = User.builder()
                    .firstName("Charlie")
                    .lastName("Customer")
                    .email("customer@insurance.com")
                    .password(passwordEncoder.encode("Customer@1234"))
                    .role(Role.CUSTOMER)
                    .enabled(true)
                    .accountLocked(false)
                    .failedLoginAttempts(0)
                    .build();
            userRepository.save(customerUser);

            if (!customerRepository.existsByEmail("customer@insurance.com")) {
                Customer customer = Customer.builder()
                        .firstName("Charlie")
                        .lastName("Customer")
                        .email("customer@insurance.com")
                        .phoneNumber("9988776655")
                        .dateOfBirth(LocalDate.of(1990, 5, 20))
                        .gender(Gender.MALE)
                        .address("123 Marine Drive")
                        .city("Mumbai")
                        .state("Maharashtra")
                        .postalCode("400001")
                        .status(CustomerStatus.ACTIVE)
                        .build();
                customerRepository.save(customer);
            }
            log.info("Default Customer account seeded: customer@insurance.com / Customer@1234");
        }
    }
}
