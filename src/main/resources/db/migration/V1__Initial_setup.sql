CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE category
(
    id   UUID NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE company
(
    user_id         UUID NOT NULL,
    company_name    VARCHAR(255),
    description     VARCHAR(255),
    foundation_date date,
    CONSTRAINT pk_company PRIMARY KEY (user_id)
);

CREATE TABLE discount
(
    id               UUID NOT NULL,
    discount_price   DOUBLE PRECISION,
    start_utc_time   TIMESTAMP WITHOUT TIME ZONE,
    end_utc_time     TIMESTAMP WITHOUT TIME ZONE,
    product_price_id UUID,
    CONSTRAINT pk_discount PRIMARY KEY (id)
);

CREATE TABLE "order"
(
    id                UUID NOT NULL,
    creation_utc_time TIMESTAMP WITHOUT TIME ZONE,
    bank_data         VARCHAR(255),
    user_id           UUID,
    order_status      VARCHAR(255),
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE order_item
(
    id           UUID NOT NULL,
    price        DOUBLE PRECISION,
    price_locale VARCHAR(255),
    license_key  VARCHAR(255),
    order_id     UUID,
    product_id   UUID,
    item_status  VARCHAR(255),
    CONSTRAINT pk_orderitem PRIMARY KEY (id)
);

CREATE TABLE person
(
    user_id     UUID NOT NULL,
    first_name  VARCHAR(255),
    last_name VARCHAR(255),
    nick_name   VARCHAR(255),
    birthday    date,
    CONSTRAINT pk_person PRIMARY KEY (user_id)
);

CREATE TABLE product
(
    id                UUID NOT NULL,
    name              VARCHAR(255),
    description       VARCHAR(255),
    parent_product_id UUID,
    user_id           UUID,
    product_status    VARCHAR(255),
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE product_category
(
    category_id UUID NOT NULL,
    product_id  UUID NOT NULL,
    CONSTRAINT pk_product_category PRIMARY KEY (category_id, product_id)
);

CREATE TABLE product_price
(
    id         UUID NOT NULL,
    price      DOUBLE PRECISION,
    locale     VARCHAR(255),
    product_id UUID,
    CONSTRAINT pk_productprice PRIMARY KEY (id)
);

CREATE TABLE role
(
    id   UUID NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id       UUID NOT NULL,
    email    VARCHAR(255),
    password VARCHAR(255),
    balance  DOUBLE PRECISION,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    role_id UUID NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (role_id, user_id)
);

ALTER TABLE company
    ADD CONSTRAINT FK_COMPANY_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

ALTER TABLE discount
    ADD CONSTRAINT FK_DISCOUNT_ON_PRODUCT_PRICE FOREIGN KEY (product_price_id) REFERENCES product_price (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDERITEM_ON_ORDER FOREIGN KEY (order_id) REFERENCES "order" (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDERITEM_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

ALTER TABLE person
    ADD CONSTRAINT FK_PERSON_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

ALTER TABLE product_price
    ADD CONSTRAINT FK_PRODUCTPRICE_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_PARENT_PRODUCT FOREIGN KEY (parent_product_id) REFERENCES product (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

ALTER TABLE product_category
    ADD CONSTRAINT fk_procat_on_category FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE product_category
    ADD CONSTRAINT fk_procat_on_product FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_role FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_user FOREIGN KEY (user_id) REFERENCES "user" (id);