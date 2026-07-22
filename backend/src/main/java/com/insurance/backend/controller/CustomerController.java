package com.insurance.backend.controller;

import com.insurance.backend.constant.ApiConstants;
import com.insurance.backend.dto.request.CustomerRequestDto;
import com.insurance.backend.dto.response.CustomerResponseDto;
import com.insurance.backend.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.CUSTOMERS)
@RequiredArgsConstructor
@Tag(
        name = "Customer Management",
        description = "REST APIs for managing insurance customers."
)
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(
            summary = "Create Customer",
            description = "Creates a new customer in the insurance management system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Email or phone number already exists")
    })
    public ResponseEntity<CustomerResponseDto> createCustomer(
            @Valid @RequestBody CustomerRequestDto requestDto) {

        final CustomerResponseDto response =
                customerService.createCustomer(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Customer By ID",
            description = "Retrieves a customer using the customer ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerResponseDto> getCustomerById(

            @Parameter(
                    name = "id",
                    description = "Customer ID",
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                customerService.getCustomerById(id));
    }

    @GetMapping
    @Operation(
            summary = "Get All Customers",
            description = "Retrieves all registered customers."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    })
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {

        return ResponseEntity.ok(
                customerService.getAllCustomers());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Customer",
            description = "Updates an existing customer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "409", description = "Email or phone number already exists")
    })
    public ResponseEntity<CustomerResponseDto> updateCustomer(

            @Parameter(
                    name = "id",
                    description = "Customer ID",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody CustomerRequestDto requestDto) {

        return ResponseEntity.ok(
                customerService.updateCustomer(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Customer",
            description = "Deletes a customer using the customer ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<Void> deleteCustomer(

            @Parameter(
                    name = "id",
                    description = "Customer ID",
                    example = "1"
            )
            @PathVariable Long id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }
}