package com.netcracker.ncstore.model;

import com.netcracker.ncstore.model.enumerations.EProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Class that defines one product in the store.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;

    @OneToOne
    @JoinColumn(name = "parent_product_id")
    private Product parentProduct;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User supplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private EProductStatus productStatus;

    @OneToMany(mappedBy = "product")
    private List<ProductPrice> productPrices;

}
