package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.dto.request.UpdateProductRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductIdUpdateRequestAuthDTO {
    private final String productId;
    private final UpdateProductRequest request;
    private final UserEmailAndRolesDTO userEmailAndRolesDTO;
}
