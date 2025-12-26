package com.creditcard.customer.controller;


import com.creditcard.customer.dto.ApiResponse;
import com.creditcard.customer.dto.CustomerRequestDTO;
import com.creditcard.customer.dto.CustomerResponseDTO;
import com.creditcard.customer.dto.CustomerUpdateDTO;
import com.creditcard.customer.enums.CustomerStatus;
import com.creditcard.customer.service.CustomerService;
import com.creditcard.customer.utill.ValidationUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
//demo
@Tag(name = "Customer Management", description = "APIs for managing customers")
//@CrossOrigin(origins = "*")
public class CustomerController {
	
    private final CustomerService customerService;


    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(
            @Valid @RequestBody CustomerRequestDTO requestDTO) {
        
        log.info("REST request to create customer: {}", requestDTO.getEmail());
        
        // Additional runtime validation
        if (!ValidationUtils.isValidLength(requestDTO.getAddress(), 10, 500)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Address must be between 10 and 500 characters"));
        }
        
        CustomerResponseDTO response = customerService.createCustomer(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Customer created successfully"));
    }          
    
    

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieves a customer by their ID")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        log.info("REST request to get customer: {}", id);
        
        CustomerResponseDTO response = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Customer retrieved successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieves all customers with pagination")
    public ResponseEntity<ApiResponse<Page<CustomerResponseDTO>>> getAllCustomers(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "ASC") String sortDir) {
        log.info("REST request to get all customers - page: {}, size: {}", page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CustomerResponseDTO> response = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "Customers retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Updates an existing customer")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateDTO updateDTO) {
        log.info("REST request to update customer: {}", id);
        
        CustomerResponseDTO response = customerService.updateCustomer(id, updateDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Customer updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Deletes a customer by ID")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        log.info("REST request to delete customer: {}", id);
        
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer deleted successfully"));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update customer status", description = "Updates the status of a customer")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomerStatus(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Parameter(description = "New status") @RequestParam CustomerStatus status) {
        log.info("REST request to update customer status: {} to {}", id, status);
        
        CustomerResponseDTO response = customerService.updateCustomerStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(response, "Customer status updated successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers", description = "Search customers by keyword")
    public ResponseEntity<ApiResponse<Page<CustomerResponseDTO>>> searchCustomers(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.info("REST request to search customers with keyword: {}", keyword);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerResponseDTO> response = customerService.searchCustomers(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "Search results retrieved successfully"));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get customers by status", description = "Retrieves customers by their status")
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> getCustomersByStatus(
            @Parameter(description = "Customer status") @PathVariable CustomerStatus status) {
        log.info("REST request to get customers by status: {}", status);
        
        List<CustomerResponseDTO> response = customerService.getCustomersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(response, "Customers retrieved successfully"));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerByEmail(
            @PathVariable String email) {
        
        log.info("REST request to get customer by email: {}", email);
        
        // Validate email format before processing
        if (!ValidationUtils.isValidEmail(email)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid email format"));
        }
        
        CustomerResponseDTO response = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(response, "Customer retrieved successfully"));
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Count customers by status", description = "Returns count of customers by status")
    public ResponseEntity<ApiResponse<Long>> getCustomerCountByStatus(
            @Parameter(description = "Customer status") @PathVariable CustomerStatus status) {
        log.info("REST request to count customers by status: {}", status);
        long count = customerService.getCustomerCountByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(count, "Customer count retrieved successfully"));
    }

    @GetMapping("/exists/email/{email}")
    @Operation(summary = "Check if email exists", description = "Checks if a customer with given email exists")
    public ResponseEntity<ApiResponse<Boolean>> existsByEmail(
            @Parameter(description = "Email to check") @PathVariable String email) {
        log.info("REST request to check if email exists: {}", email);
        
        boolean exists = customerService.existsByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(exists, "Email check completed"));
    }
    
    @GetMapping("/exists/{id}")
    @Operation(summary = "Check if customer exists by ID", description = "Checks if a customer with given ID exists")
    public ResponseEntity<ApiResponse<Boolean>> existsById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        log.info("REST request to check if customer exists with ID: {}", id);
        
        boolean exists = customerService.existsById(id);
        return ResponseEntity.ok(ApiResponse.success(exists, 
            exists ? "Customer exists" : "Customer does not exist"));
    }
    
    
   
}