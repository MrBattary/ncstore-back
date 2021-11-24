package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductIdAuthDTO {
    private final String productId;
    private final UserEmailAndRolesDTO userEmailAndRolesDTO;
}
