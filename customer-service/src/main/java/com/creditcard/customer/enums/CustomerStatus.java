package com.creditcard.customer.enums;




/**
 * Enum representing customer account status
 */
public enum CustomerStatus {
    ACTIVE("Active", "Customer account is active"),
    INACTIVE("Inactive", "Customer account is inactive"),
    SUSPENDED("Suspended", "Customer account is temporarily suspended"),
    BLOCKED("Blocked", "Customer account is permanently blocked");

    private final String displayName;
    private final String description;

    CustomerStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
