package com.creditcard.customer.utill;



public class Constants {
    
    // API Constants
    public static final String API_BASE_PATH = "/api";
    public static final String CUSTOMERS_PATH = "/customers";
    
    // Validation Messages
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Email should be valid";
    public static final String PHONE_REQUIRED = "Phone number is required";
    public static final String PHONE_PATTERN = "Phone number must be 10 digits";
    
    // Error Messages
    public static final String CUSTOMER_NOT_FOUND = "Customer not found with ID: ";
    public static final String CUSTOMER_EMAIL_EXISTS = "Customer with email already exists: ";
    public static final String CUSTOMER_PHONE_EXISTS = "Customer with phone already exists: ";
    
    // Success Messages
    public static final String CUSTOMER_CREATED = "Customer created successfully";
    public static final String CUSTOMER_UPDATED = "Customer updated successfully";
    public static final String CUSTOMER_DELETED = "Customer deleted successfully";
    public static final String CUSTOMER_STATUS_UPDATED = "Customer status updated successfully";
    
    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIR = "ASC";
    
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}