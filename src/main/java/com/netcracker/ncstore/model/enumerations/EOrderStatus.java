package com.netcracker.ncstore.model.enumerations;

/**
 * Enum used for order statuses
 */
public enum EOrderStatus {
    /**
     * Order was created but not paid yet
     */
    UNPAID,
    /**
     * Order paid
     */
    PAID,
    /**
     * Order complied and user received one's products.
     */
    COMPLETED,
}
