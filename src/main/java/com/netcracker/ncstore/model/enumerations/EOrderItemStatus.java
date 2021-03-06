package com.netcracker.ncstore.model.enumerations;

/**
 * Enum used for items in order.
 * For example, order includes several products,
 * and 1 or more products could be refunded. That is why
 * we need to know the status of each item in order.
 */
public enum EOrderItemStatus {
    /**
     * Order is registered but not paid, so user can't use item.
     * Such item may only have Product and Order references without any price, key and region
     */
    REGISTERED,
    /**
     * Order is paid, item is paid, but no licence key assigned
     */
    PAID,
    /**
     * Product is ready to use.
     */
    COMPLETED,
    /**
     * User requested a refund for this product.
     * User can not use product anymore.
     */
    REFUND_REQUESTED,
    /**
     * Refund was completed and user received one's
     * money back. User can not use product anymore.
     */
    REFUNDED
}
