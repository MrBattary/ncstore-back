package com.netcracker.ncstore.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class PaymentPostRequest {
    private final BigDecimal amount;
    private final String nonce;
}
