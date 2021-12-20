package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductStatistic {
    @Id
    private UUID id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int totalSalesAmount;

    private double averageRating;
    private int totalReviewCount;

    private int oneStarReviewCount;
    private int twoStarsReviewCount;
    private int threeStarsReviewCount;
    private int fourStarsReviewCount;
    private int fiveStarsReviewCount;

    public ProductStatistic(Product product) {
        this.product = product;
    }
}
