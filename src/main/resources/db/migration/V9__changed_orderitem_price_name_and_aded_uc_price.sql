ALTER TABLE order_item
    ADD price_uc DOUBLE PRECISION;

ALTER TABLE order_item
    RENAME price TO localized_price