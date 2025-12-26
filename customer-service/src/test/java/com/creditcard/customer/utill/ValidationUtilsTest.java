package com.creditcard.customer.utill;



import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void testIsValidEmail_ValidEmail_ReturnsTrue() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"));
        assertTrue(ValidationUtils.isValidEmail("user.name@domain.co.uk"));
        assertTrue(ValidationUtils.isValidEmail("user+tag@example.com"));
        
    }

    @Test
    void testIsValidEmail_InvalidEmail_ReturnsFalse() {
        assertFalse(ValidationUtils.isValidEmail("invalid.email"));
        assertFalse(ValidationUtils.isValidEmail("@example.com"));
        assertFalse(ValidationUtils.isValidEmail("test@"));
        assertFalse(ValidationUtils.isValidEmail(null));
        assertFalse(ValidationUtils.isValidEmail(""));
    }

    @Test
    void testIsValidPhone_ValidPhone_ReturnsTrue() {
        assertTrue(ValidationUtils.isValidPhone("1234567890"));
        assertTrue(ValidationUtils.isValidPhone("9876543210"));
    }

    @Test
    void testIsValidPhone_InvalidPhone_ReturnsFalse() {
        assertFalse(ValidationUtils.isValidPhone("123456789")); // 9 digits
        assertFalse(ValidationUtils.isValidPhone("12345678901")); // 11 digits
        assertFalse(ValidationUtils.isValidPhone("12345abcde"));
        assertFalse(ValidationUtils.isValidPhone(null));
        assertFalse(ValidationUtils.isValidPhone(""));
    }

    @Test
    void testIsValidZipCode_ValidZipCode_ReturnsTrue() {
        assertTrue(ValidationUtils.isValidZipCode("12345"));
        assertTrue(ValidationUtils.isValidZipCode("123456789"));
        assertTrue(ValidationUtils.isValidZipCode("1234567890"));
    }

    @Test
    void testIsValidZipCode_InvalidZipCode_ReturnsFalse() {
        assertFalse(ValidationUtils.isValidZipCode("1234")); // 4 digits
        assertFalse(ValidationUtils.isValidZipCode("12345678901")); // 11 digits
        assertFalse(ValidationUtils.isValidZipCode("abcde"));
        assertFalse(ValidationUtils.isValidZipCode(null));
    }

    @Test
    void testIsValidName_ValidName_ReturnsTrue() {
        assertTrue(ValidationUtils.isValidName("John"));
        assertTrue(ValidationUtils.isValidName("Mary Jane"));
        assertTrue(ValidationUtils.isValidName("O'Brien"));
        assertTrue(ValidationUtils.isValidName("Smith-Jones"));
    }

    @Test
    void testIsValidName_InvalidName_ReturnsFalse() {
        assertFalse(ValidationUtils.isValidName("J")); // Too short
        assertFalse(ValidationUtils.isValidName("John123"));
        assertFalse(ValidationUtils.isValidName("John@Doe"));
        assertFalse(ValidationUtils.isValidName(null));
    }

    @Test
    void testIsValidLength_ValidLength_ReturnsTrue() {
        assertTrue(ValidationUtils.isValidLength("Hello", 2, 10));
        assertTrue(ValidationUtils.isValidLength("Hi", 2, 10));
        assertTrue(ValidationUtils.isValidLength("HelloWorld", 2, 10));
    }

    @Test
    void testIsValidLength_InvalidLength_ReturnsFalse() {
        assertFalse(ValidationUtils.isValidLength("H", 2, 10)); // Too short
        assertFalse(ValidationUtils.isValidLength("HelloWorldExtra", 2, 10)); // Too long
        assertFalse(ValidationUtils.isValidLength(null, 2, 10));
    }

    @Test
    void testIsPastDate_PastDate_ReturnsTrue() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        assertTrue(ValidationUtils.isPastDate(pastDate));
    }

    @Test
    void testIsPastDate_FutureDate_ReturnsFalse() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        assertFalse(ValidationUtils.isPastDate(futureDate));
        assertFalse(ValidationUtils.isPastDate(null));
    }

    @Test
    void testIsValidAge_ValidAge_ReturnsTrue() {
        LocalDateTime twentyYearsAgo = LocalDateTime.now().minusYears(20);
        assertTrue(ValidationUtils.isValidAge(twentyYearsAgo));
    }

    @Test
    void testIsValidAge_InvalidAge_ReturnsFalse() {
        LocalDateTime tenYearsAgo = LocalDateTime.now().minusYears(10);
        assertFalse(ValidationUtils.isValidAge(tenYearsAgo));
        assertFalse(ValidationUtils.isValidAge(null));
    }

    @Test
    void testFormatPhoneNumber() {
        assertEquals("123-456-7890", ValidationUtils.formatPhoneNumber("1234567890"));
        assertNull(ValidationUtils.formatPhoneNumber(null));
    }

    @Test
    void testUnformatPhoneNumber() {
        assertEquals("1234567890", ValidationUtils.unformatPhoneNumber("123-456-7890"));
        assertEquals("1234567890", ValidationUtils.unformatPhoneNumber("(123) 456-7890"));
        assertNull(ValidationUtils.unformatPhoneNumber(null));
    }

    @Test
    void testIsValidStateCode_ValidState_ReturnsTrue() {
        assertTrue(ValidationUtils.isValidStateCode("NY"));
        assertTrue(ValidationUtils.isValidStateCode("CA"));
        assertTrue(ValidationUtils.isValidStateCode("tx")); // lowercase
    }

    @Test
    void testIsValidStateCode_InvalidState_ReturnsFalse() {
        assertFalse(ValidationUtils.isValidStateCode("XX"));
        assertFalse(ValidationUtils.isValidStateCode("ABC"));
        assertFalse(ValidationUtils.isValidStateCode(null));
    }

    @Test
    void testCapitalizeWords() {
        assertEquals("John Doe", ValidationUtils.capitalizeWords("john doe"));
        assertEquals("Mary Jane Smith", ValidationUtils.capitalizeWords("MARY JANE SMITH"));
        assertNull(ValidationUtils.capitalizeWords(null));
    }

    @Test
    void testMaskEmail() {
        assertEquals("j***e@example.com", ValidationUtils.maskEmail("john.doe@example.com"));
        assertEquals("a*****n@test.com", ValidationUtils.maskEmail("admin@test.com"));
    }

    @Test
    void testMaskPhone() {
        assertEquals("XXX-XXX-7890", ValidationUtils.maskPhone("1234567890"));
        assertNull(ValidationUtils.maskPhone(null));
    }

    @Test
    void testIsNumeric_Numeric_ReturnsTrue() {
        assertTrue(ValidationUtils.isNumeric("12345"));
        assertTrue(ValidationUtils.isNumeric("0"));
    }

    @Test
    void testIsNumeric_NotNumeric_ReturnsFalse() {
        assertFalse(ValidationUtils.isNumeric("12345a"));
        assertFalse(ValidationUtils.isNumeric("12.34"));
        assertFalse(ValidationUtils.isNumeric(null));
    }

    @Test
    void testSanitizeInput() {
        assertEquals("Hello World", ValidationUtils.sanitizeInput("Hello<> World"));
        assertEquals("TestData", ValidationUtils.sanitizeInput("Test'Data\""));
        assertNull(ValidationUtils.sanitizeInput(null));
    }

    @Test
    void testTruncate() {
        assertEquals("Hello", ValidationUtils.truncate("Hello World", 5));
        assertEquals("Hello", ValidationUtils.truncate("Hello", 10));
        assertNull(ValidationUtils.truncate(null, 5));
       
    }
}
