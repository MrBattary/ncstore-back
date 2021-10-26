package com.netcracker.ncstore.model;

import com.netcracker.ncstore.model.enumerations.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

/**
 * Class that defines a single order of user.
 * Order consists of some number of products.
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private long id;
    private long userId;
    private Instant instantOfCreation;
    private EOrderStatus status;
    private String bankData;

    private List<OrderItem> products;
}
