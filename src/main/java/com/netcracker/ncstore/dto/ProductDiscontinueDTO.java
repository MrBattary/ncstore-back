package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductDiscontinueDTO {
    private final UUID productId;
    private final User issuer;
}
