package com.creditcard.customer.utill;



import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Utility class for common validation operations
 * Contains reusable validation methods for customer data
 */
public class ValidationUtils {

    // Regular Expressions
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9]{10}$"
    );

    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile(
            "^[0-9]{5,10}$"
    );

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[A-Za-z\\s'-]{2,100}$"
    );

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile(
            "^[A-Za-z0-9\\s]+$"
    );

    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new IllegalStateException("Utility class - cannot be instantiated");
    }

    /**
     * Validates email format
     *
     * @param email Email address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates phone number (10 digits)
     *
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validates zip code (5-10 digits)
     *
     * @param zipCode Zip code to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidZipCode(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            return false;
        }
        return ZIP_CODE_PATTERN.matcher(zipCode.trim()).matches();
    }

    /**
     * Validates name (letters, spaces, hyphens, apostrophes)
     *
     * @param name Name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates if string is alphanumeric
     *
     * @param input String to validate
     * @return true if alphanumeric, false otherwise
     */
    public static boolean isAlphanumeric(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return ALPHANUMERIC_PATTERN.matcher(input.trim()).matches();
    }

    /**
     * Validates if string is not null or empty
     *
     * @param input String to validate
     * @return true if not blank, false otherwise
     */
    public static boolean isNotBlank(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Validates string length
     *
     * @param input String to validate
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @return true if within range, false otherwise
     */
    public static boolean isValidLength(String input, int minLength, int maxLength) {
        if (input == null) {
            return false;
        }
        int length = input.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validates if date is in the past
     *
     * @param date Date to validate
     * @return true if in past, false otherwise
     */
    public static boolean isPastDate(LocalDateTime date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDateTime.now());
    }

    /**
     * Validates if date is in the future
     *
     * @param date Date to validate
     * @return true if in future, false otherwise
     */
    public static boolean isFutureDate(LocalDateTime date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDateTime.now());
    }

    /**
     * Validates age (must be 18 or older)
     *
     * @param dateOfBirth Date of birth
     * @return true if 18+, false otherwise
     */
    public static boolean isValidAge(LocalDateTime dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        LocalDateTime eighteenYearsAgo = LocalDateTime.now().minusYears(18);
        return dateOfBirth.isBefore(eighteenYearsAgo);
    }

    /**
     * Sanitizes string input (removes special characters)
     *
     * @param input String to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("[<>\"']", "");
    }

    /**
     * Formats phone number (adds dashes)
     * Example: 1234567890 -> 123-456-7890
     *
     * @param phone Phone number
     * @return Formatted phone number
     */
    public static String formatPhoneNumber(String phone) {
        if (phone == null || phone.length() != 10) {
            return phone;
        }
        return phone.substring(0, 3) + "-" + 
               phone.substring(3, 6) + "-" + 
               phone.substring(6, 10);
    }

    /**
     * Removes phone number formatting
     * Example: 123-456-7890 -> 1234567890
     *
     * @param phone Formatted phone number
     * @return Unformatted phone number
     */
    public static String unformatPhoneNumber(String phone) {
        if (phone == null) {
            return null;
        }
        return phone.replaceAll("[^0-9]", "");
    }

    /**
     * Validates US state code (2 letters)
     *
     * @param state State code
     * @return true if valid, false otherwise
     */
    public static boolean isValidStateCode(String state) {
        if (state == null || state.trim().isEmpty()) {
            return false;
        }
        String[] validStates = {
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
            "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
            "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
            "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
            "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
        };
        
        String upperState = state.trim().toUpperCase();
        for (String validState : validStates) {
            if (validState.equals(upperState)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Capitalizes first letter of each word
     * Example: "john doe" -> "John Doe"
     *
     * @param input String to capitalize
     * @return Capitalized string
     */
    public static String capitalizeWords(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }

    /**
     * Masks email for security
     * Example: john.doe@example.com -> j***e@example.com
     *
     * @param email Email to mask
     * @return Masked email
     */
    public static String maskEmail(String email) {
        if (email == null || !isValidEmail(email)) {
            return email;
        }
        
        String[] parts = email.split("@");
        String localPart = parts[0];
        
        if (localPart.length() <= 2) {
            return email;
        }
        
        String masked = localPart.charAt(0) + 
                       "*".repeat(localPart.length() - 2) + 
                       localPart.charAt(localPart.length() - 1);
        
        return masked + "@" + parts[1];
    }

    /**
     * Masks phone number for security
     * Example: 1234567890 -> XXX-XXX-7890
     *
     * @param phone Phone to mask
     * @return Masked phone
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() != 10) {
            return phone;
        }
        return "XXX-XXX-" + phone.substring(6, 10);
    }

    /**
     * Validates if two strings match
     *
     * @param str1 First string
     * @param str2 Second string
     * @return true if match, false otherwise
     */
    public static boolean matches(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * Checks if string contains only digits
     *
     * @param input String to check
     * @return true if numeric, false otherwise
     */
    public static boolean isNumeric(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return input.trim().matches("^[0-9]+$");
    }

    /**
     * Validates minimum age requirement
     *
     * @param dateOfBirth Date of birth
     * @param minAge Minimum age required
     * @return true if meets requirement, false otherwise
     */
    public static boolean meetsMinimumAge(LocalDateTime dateOfBirth, int minAge) {
        if (dateOfBirth == null) {
            return false;
        }
        LocalDateTime minAgeDate = LocalDateTime.now().minusYears(minAge);
        return dateOfBirth.isBefore(minAgeDate);
    }

    /**
     * Truncates string to max length
     *
     * @param input String to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    public static String truncate(String input, int maxLength) {
        if (input == null) {
            return null;
        }
        if (input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength);
    }

    /**
     * Converts string to uppercase
     *
     * @param input String to convert
     * @return Uppercase string
     */
    public static String toUpperCase(String input) {
        return input == null ? null : input.trim().toUpperCase();
    }

    /**
     * Converts string to lowercase
     *
     * @param input String to convert
     * @return Lowercase string
     */
    public static String toLowerCase(String input) {
        return input == null ? null : input.trim().toLowerCase();
    }
}