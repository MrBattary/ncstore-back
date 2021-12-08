package com.netcracker.ncstore.model;

import com.netcracker.ncstore.model.enumerations.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Class that defines a single order of user.
 * Order consists of some number of products.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Order {
    @Id
    @GeneratedValue
    private UUID id;
    private Instant creationUtcTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = javax.persistence.FetchType.EAGER, mappedBy = "order")
    private List<OrderItem> products;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private EOrderStatus orderStatus;

    public Order(Instant creationUtcTime, User user, EOrderStatus orderStatus) {
        this.creationUtcTime = creationUtcTime;
        this.user = user;
        this.orderStatus = orderStatus;
    }
}
