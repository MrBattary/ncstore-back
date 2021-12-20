package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductReview {
    @Id
    @GeneratedValue
    private UUID id;

    private Instant creationTimeUtc;

    private int productRating;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String reviewText;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    public ProductReview(Instant creationTimeUtc, int productRating, String reviewText, Product product, User author) {
        this.creationTimeUtc = creationTimeUtc;
        this.productRating = productRating;
        this.reviewText = reviewText;
        this.product = product;
        this.author = author;
    }
}
