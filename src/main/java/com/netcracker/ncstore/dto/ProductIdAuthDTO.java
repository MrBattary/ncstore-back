package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductIdAuthDTO {
    private final UUID productId;
    private final UserEmailAndRolesDTO userEmailAndRolesDTO;
}
