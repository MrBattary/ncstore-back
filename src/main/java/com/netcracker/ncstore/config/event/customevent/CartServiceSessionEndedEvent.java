package com.netcracker.ncstore.config.event.customevent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

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

}
