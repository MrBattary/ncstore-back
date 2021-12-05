CREATE TABLE price_conversion_rate
(
    id                    UUID NOT NULL,
    region                VARCHAR(255),
    universal_price_value DOUBLE PRECISION,
    CONSTRAINT pk_priceconversionrate PRIMARY KEY (id)
);

ALTER TABLE price_conversion_rate
    ADD CONSTRAINT uc_priceconversionrate_region UNIQUE (region);

ALTER TABLE cart_item
    ALTER COLUMN cart_id DROP NOT NULL;