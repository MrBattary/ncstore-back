alter table discount drop constraint fk_discount_on_product_price;

alter table discount
    add constraint fk_discount_on_product_price
        foreign key (product_price_id) references product_price
            on delete cascade;
