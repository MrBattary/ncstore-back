CREATE TABLE product_review
(
    id                UUID NOT NULL,
    creation_time_utc TIMESTAMP WITHOUT TIME ZONE,
    product_rating    INTEGER,
    review_text       TEXT,
    product_id        UUID NOT NULL,
    user_id           UUID NOT NULL,
    CONSTRAINT pk_productreview PRIMARY KEY (id)
);

CREATE TABLE product_statistic
(
    product_id               UUID NOT NULL,
    total_sales_amount       INTEGER,
    average_rating           DOUBLE PRECISION,
    total_review_count       INTEGER,
    one_star_review_count    INTEGER,
    two_stars_review_count   INTEGER,
    three_stars_review_count INTEGER,
    four_stars_review_count  INTEGER,
    five_stars_review_count  INTEGER,
    CONSTRAINT pk_productstatistic PRIMARY KEY (product_id)
);

ALTER TABLE product_review
    ADD CONSTRAINT FK_PRODUCTREVIEW_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE product_review
    ADD CONSTRAINT FK_PRODUCTREVIEW_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

ALTER TABLE product_statistic
    ADD CONSTRAINT FK_PRODUCTSTATISTIC_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);