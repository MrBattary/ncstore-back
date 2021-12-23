package com.netcracker.ncstore.dto.request;

import com.netcracker.ncstore.dto.CartItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Getter
public class OrderCreateRequest {
    private final List<CartItemDTO> productsToBuyWithCount;
    private final String customerEmail;
    private final Locale region;
    private final boolean useBalance;
    private final String nonce;
}
