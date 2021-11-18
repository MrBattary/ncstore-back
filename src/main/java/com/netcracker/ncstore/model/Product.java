package com.netcracker.ncstore.model;

import com.netcracker.ncstore.model.enumerations.EProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
    @GeneratedValue
    private UUID id;
    private String name;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String description;
    private Instant creationUtcTime;

    @OneToOne
    @JoinColumn(name = "parent_product_id")
    private Product parentProduct;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User supplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private EProductStatus productStatus;

    @OneToMany(mappedBy = "product")
    private List<ProductPrice> productPrices;

    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;


}
