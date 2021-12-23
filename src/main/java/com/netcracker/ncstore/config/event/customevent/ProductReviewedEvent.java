package com.netcracker.ncstore.config.event.customevent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class ProductReviewedEvent extends ApplicationEvent {
    @Getter
    private final UUID productId;
    @Getter
    private final int rating;

    public ProductReviewedEvent(Object source, UUID productId, int rating) {
        super(source);
        this.productId = productId;
        this.rating = rating;
    }
}
