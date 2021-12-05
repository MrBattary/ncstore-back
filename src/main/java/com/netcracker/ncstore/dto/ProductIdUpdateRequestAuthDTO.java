package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.dto.request.ProductUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductIdUpdateRequestAuthDTO {
    private final UUID productId;
    private final ProductUpdateRequest request;
    private final UserEmailAndRolesDTO userEmailAndRolesDTO;
}
