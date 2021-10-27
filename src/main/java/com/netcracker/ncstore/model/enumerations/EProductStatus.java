package com.netcracker.ncstore.model.enumerations;

/**
 * Enum used for status of product in the store.
 */
public enum EProductStatus {
    /**
     * Product is not released yet. Users can not buy it.
     */
    NOT_RELEASED,
    /**
     * Product is in the store and users can buy it.
     */
    IN_STOCK,
    /**
     * Product is in the store, but is out of stock, so users can not buy it.
     */
    OUT_OF_STOCK,
    /**
     * Product was in the store and now is not for sale.
     */
    DISCONTINUED
}
