package com.creditcard.customer.controller;




import com.creditcard.customer.dto.CustomerRequestDTO;
import com.creditcard.customer.dto.CustomerResponseDTO;
import com.creditcard.customer.enums.CustomerStatus;
import com.creditcard.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private CustomerRequestDTO requestDTO;
    private CustomerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
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
    void testCreateCustomer_Success() throws Exception {
        // Arrange
        when(customerService.createCustomer(any(CustomerRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    void testGetCustomerById_Success() throws Exception {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    void testCreateCustomer_ValidationError() throws Exception {
        // Arrange
        requestDTO.setEmail("invalid-email");

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}