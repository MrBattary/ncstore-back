package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserIdProductIdDTO {
    private final String userEmail;
    private final UUID productId;
}
