package com.netcracker.ncstore.dto.create;

import com.netcracker.ncstore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderCreateDTO {
    private final Map<UUID, Integer> productsToBuyWithCount;
    private final User customer;
}
