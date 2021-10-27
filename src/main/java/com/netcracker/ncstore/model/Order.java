package com.netcracker.ncstore.model;

import com.netcracker.ncstore.model.enumerations.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Instant instantOfCreation;
    private String bankData;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> products;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private EOrderStatus orderStatus;

}
