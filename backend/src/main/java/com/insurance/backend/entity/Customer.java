package com.insurance.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers",
        indexes = {
                @Index(name = "idx_customer_email", columnList = "email"),
                @Index(name = "idx_customer_phone", columnList = "phoneNumber")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z]+([ '-][A-Za-z]+)*$",
            message = "First name can contain only letters and spaces"
    )
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z]+([ '-][A-Za-z]+)*$",
            message = "Last name can contain only letters and spaces"
    )
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique= true, length = 150)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian mobile number"
    )
    @Column(nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Past(message = "Date of birth must be in the past")
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @NotBlank(message = "Address is required")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "City can contain only letters and spaces"
    )
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100)
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "State can contain only letters and spaces"
    )
    @Column(nullable = false, length = 100)
    private String state;

    @NotBlank(message = "Postal code is required")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Invalid PIN code"
    )
    @Column(nullable = false, length = 6)
    private String postalCode;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CustomerStatus status = CustomerStatus.ACTIVE;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "customer",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Policy> policies = new ArrayList<>();
}
