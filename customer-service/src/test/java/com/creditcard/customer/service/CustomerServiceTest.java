package com.creditcard.customer.service;



import com.creditcard.customer.dto.CustomerRequestDTO;
import com.creditcard.customer.dto.CustomerResponseDTO;
import com.creditcard.customer.entity.Customer;
import com.creditcard.customer.enums.CustomerStatus;
import com.creditcard.customer.exception.CustomerNotFoundException;
import com.creditcard.customer.exception.DuplicateCustomerException;
import com.creditcard.customer.mapper.CustomerMapper;
import com.creditcard.customer.repository.CustomerRepository;
import com.creditcard.customer.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequestDTO requestDTO;
    private CustomerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .address("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .status(CustomerStatus.ACTIVE)
                .build();

        requestDTO = CustomerRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .address("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .build();

        responseDTO = CustomerResponseDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .fullName("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .status(CustomerStatus.ACTIVE)
                .build();
    }

    @Test
    void testCreateCustomer_Success() {
        // Arrange
        when(customerRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(customerRepository.existsByPhone(requestDTO.getPhone())).thenReturn(false);
        when(customerMapper.toEntity(requestDTO)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDTO(customer)).thenReturn(responseDTO);

        // Act
        CustomerResponseDTO result = customerService.createCustomer(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testCreateCustomer_DuplicateEmail_ThrowsException() {
        // Arrange
        when(customerRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateCustomerException.class, () -> {
            customerService.createCustomer(requestDTO);
        });
        
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(responseDTO);

        // Act
        CustomerResponseDTO result = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void testGetCustomerById_NotFound_ThrowsException() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerById(1L);
        });
    }

    @Test
    void testDeleteCustomer_Success() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomer_NotFound_ThrowsException() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.deleteCustomer(1L);
        });
        
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateCustomerStatus_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDTO(customer)).thenReturn(responseDTO);

        // Act
        CustomerResponseDTO result = customerService.updateCustomerStatus(1L, CustomerStatus.SUSPENDED);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}