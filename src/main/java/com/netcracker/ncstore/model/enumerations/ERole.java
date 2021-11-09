package com.netcracker.ncstore.model.enumerations;

/**
 * Enum used for user roles that uses to separate privileges
 */
public enum ERole {
    /**
     * Admin role, absolute power
     */
    ADMIN,
    /**
     * Customer role, can buy products
     */
    CUSTOMER,
    /**
     * Supplier role, creates and sells products
     */
    SUPPLIER
}
