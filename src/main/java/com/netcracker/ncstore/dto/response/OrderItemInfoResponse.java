package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.model.enumerations.EOrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

/**
 * Response giving info about OrderItem
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class OrderItemInfoResponse {
    private final UUID productId;
    private final String productName;
    private final UUID supplierId;
    private final double paid;
    private final String currency;
    private final String licenseKey;
    private final EOrderItemStatus status;
}
