package com.netcracker.ncstore.config.event.customevent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class ProductOrderCompletedEvent extends ApplicationEvent {
    @Getter
    private final UUID productId;

    public ProductOrderCompletedEvent(Object source, UUID productId) {
        super(source);
        this.productId = productId;
    }
}
