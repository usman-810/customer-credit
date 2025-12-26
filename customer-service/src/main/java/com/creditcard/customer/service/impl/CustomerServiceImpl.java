package com.creditcard.customer.service.impl;



import com.creditcard.customer.config.AppProperties;
import com.creditcard.customer.dto.CustomerRequestDTO;
import com.creditcard.customer.dto.CustomerResponseDTO;
import com.creditcard.customer.dto.CustomerUpdateDTO;
import com.creditcard.customer.entity.Customer;
import com.creditcard.customer.enums.CustomerStatus;
import com.creditcard.customer.exception.CustomerNotFoundException;
import com.creditcard.customer.exception.DuplicateCustomerException;
import com.creditcard.customer.exception.InvalidCustomerDataException;
import com.creditcard.customer.mapper.CustomerMapper;
import com.creditcard.customer.repository.CustomerRepository;
import com.creditcard.customer.service.CustomerService;
import com.creditcard.customer.utill.ValidationUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AppProperties appProperties;
  
    
    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        log.info("Creating customer with email: {}", requestDTO.getEmail());

        // Additional validation using ValidationUtils
        if (!ValidationUtils.isValidEmail(requestDTO.getEmail())) {
            throw new InvalidCustomerDataException("Invalid email format");
        }

        if (!ValidationUtils.isValidPhone(requestDTO.getPhone())) {
            throw new InvalidCustomerDataException("Invalid phone format");
        }

        if (!ValidationUtils.isValidStateCode(requestDTO.getState())) {
            throw new InvalidCustomerDataException("Invalid state code");
        }

        // Sanitize inputs
        requestDTO.setFirstName(ValidationUtils.capitalizeWords(requestDTO.getFirstName()));
        requestDTO.setLastName(ValidationUtils.capitalizeWords(requestDTO.getLastName()));
        requestDTO.setEmail(ValidationUtils.toLowerCase(requestDTO.getEmail()));
        requestDTO.setState(ValidationUtils.toUpperCase(requestDTO.getState()));

        // Check for duplicate email
        if (customerRepository.existsByEmail(requestDTO.getEmail())) {
            log.error("Customer with email {} already exists", requestDTO.getEmail());
            throw new DuplicateCustomerException("Customer with email " + requestDTO.getEmail() + " already exists");
        }

        // Validate age if date of birth provided
        if (requestDTO.getDateOfBirth() != null) {
            if (!ValidationUtils.isValidAge(requestDTO.getDateOfBirth())) {
                throw new InvalidCustomerDataException("Customer must be at least 18 years old");
            }
        }

        Customer customer = customerMapper.toEntity(requestDTO);
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return customerMapper.toDTO(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new CustomerNotFoundException("Customer not found with ID: " + id);
                });

        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> getAllCustomers(Pageable pageable) {
        log.info("Fetching all customers - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(customerMapper::toDTO);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO updateDTO) {
        log.info("Updating customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new CustomerNotFoundException("Customer not found with ID: " + id);
                });

        // Check for duplicate email if email is being updated
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(updateDTO.getEmail())) {
                log.error("Email {} is already in use", updateDTO.getEmail());
                throw new DuplicateCustomerException("Email " + updateDTO.getEmail() + " is already in use");
            }
        }

        // Check for duplicate phone if phone is being updated
        if (updateDTO.getPhone() != null && !updateDTO.getPhone().equals(customer.getPhone())) {
            if (customerRepository.existsByPhone(updateDTO.getPhone())) {
                log.error("Phone {} is already in use", updateDTO.getPhone());
                throw new DuplicateCustomerException("Phone " + updateDTO.getPhone() + " is already in use");
            }
        }

        customerMapper.updateEntityFromDTO(updateDTO, customer);
        Customer updatedCustomer = customerRepository.save(customer);

        log.info("Customer updated successfully with ID: {}", id);
        return customerMapper.toDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);

        if (!customerRepository.existsById(id)) {
            log.error("Customer not found with ID: {}", id);
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }

        customerRepository.deleteById(id);
        log.info("Customer deleted successfully with ID: {}", id);
    }

    @Override
    public CustomerResponseDTO updateCustomerStatus(Long id, CustomerStatus status) {
        log.info("Updating status for customer ID: {} to {}", id, status);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new CustomerNotFoundException("Customer not found with ID: " + id);
                });

        customer.setStatus(status);
        Customer updatedCustomer = customerRepository.save(customer);

        log.info("Customer status updated successfully");
        return customerMapper.toDTO(updatedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> searchCustomers(String keyword, Pageable pageable) {
        log.info("Searching customers with keyword: {}", keyword);
        
        Page<Customer> customers = customerRepository.searchCustomers(keyword, pageable);
        return customers.map(customerMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getCustomersByStatus(CustomerStatus status) {
        log.info("Fetching customers with status: {}", status);
        
        List<Customer> customers = customerRepository.findByStatus(status);
        return customers.stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getCustomerCountByStatus(CustomerStatus status) {
        log.info("Counting customers with status: {}", status);
        return customerRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerByEmail(String email) {
        log.info("Fetching customer with email: {}", email);
        
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Customer not found with email: {}", email);
                    return new CustomerNotFoundException("Customer not found with email: " + email);
                });

        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        log.debug("Checking if customer exists with ID: {}", id);
        return customerRepository.existsById(id);
    }
}
