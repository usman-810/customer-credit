package com.creditcard.customer.service;


import com.creditcard.customer.dto.CustomerRequestDTO;
import com.creditcard.customer.dto.CustomerResponseDTO;
import com.creditcard.customer.dto.CustomerUpdateDTO;
import com.creditcard.customer.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);

    CustomerResponseDTO getCustomerById(Long id);

    Page<CustomerResponseDTO> getAllCustomers(Pageable pageable);

    CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO updateDTO);

    void deleteCustomer(Long id);

    CustomerResponseDTO updateCustomerStatus(Long id, CustomerStatus status);

    Page<CustomerResponseDTO> searchCustomers(String keyword, Pageable pageable);

    List<CustomerResponseDTO> getCustomersByStatus(CustomerStatus status);

    long getCustomerCountByStatus(CustomerStatus status);

    CustomerResponseDTO getCustomerByEmail(String email);

    boolean existsByEmail(String email);

	boolean existsById(Long id);
}
