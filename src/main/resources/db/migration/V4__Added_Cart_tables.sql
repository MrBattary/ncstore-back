CREATE TABLE cart
(
    user_id       UUID NOT NULL,
    creation_time TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_cart PRIMARY KEY (user_id)
);

CREATE TABLE cart_item
(
    id         UUID NOT NULL,
    product_id UUID,
    count      INTEGER,
    cart_id    UUID NOT NULL,
    CONSTRAINT pk_cartitem PRIMARY KEY (id)
);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CARTITEM_ON_CART FOREIGN KEY (cart_id) REFERENCES cart (user_id);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);