package com.netcracker.ncstore.config.event.customevent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.util.Map;
import java.util.UUID;

@Getter
public class CartServiceSessionEndedEvent extends ApplicationEvent {

    private Map<UUID, Integer> itemsToSafe;
    private String userEmail;

    public CartServiceSessionEndedEvent(Object source, Map<UUID, Integer> itemsToSafe, String userEmail) {
        super(source);
        this.itemsToSafe = itemsToSafe;
        this.userEmail = userEmail;
    }

    public CartServiceSessionEndedEvent(Object source, Clock clock, Map<UUID, Integer> itemsToSafe, String userEmail) {
        super(source, clock);
        this.itemsToSafe = itemsToSafe;
        this.userEmail = userEmail;
    }
}
