package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.enumerations.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class OrderDTO {
    private final UUID id;
    private final Instant creationUtcTime;
    private final String bankData;
    private final UUID userId;
    private final List<UUID> orderItemsIds;
    private final EOrderStatus orderStatus;
}
