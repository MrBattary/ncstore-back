--default and essential database data should be here
insert into role values ('0d82249f-b1d7-48a6-8818-dc22752903ed', 'ADMIN') on conflict do nothing ;
insert into role values ('1d186379-cfff-4c30-8803-482ae94a9e79', 'SUPPLIER') on conflict do nothing ;
insert into role values ('24cccce3-67a4-4267-a910-537eba38a5cb', 'CUSTOMER') on conflict do nothing ;

--test database data should be here (can be deleted any time)
insert into "user" values ('b49394f3-0f15-4f0c-ab4b-276cd736c67b', 'test','','1000000000.0') on conflict do nothing;

insert into product values ('1071989f-888e-4e3b-985d-6d9151a24619', 'product1', 'descProduct1', null, 'b49394f3-0f15-4f0c-ab4b-276cd736c67b', 'IN_STOCK', '2021-11-11 01:42:06.685000') on conflict do nothing;
insert into product values ('213f4e74-2ffe-4b5d-ac67-a3a56c1bdcf6', 'product2', 'descProduct2', null, 'b49394f3-0f15-4f0c-ab4b-276cd736c67b', 'IN_STOCK', '2021-11-11 01:42:06.685000') on conflict do nothing;
insert into product values ('a6a8eaba-dbde-488f-989b-7ed81b885c2d', 'product3', 'descProduct3', null, 'b49394f3-0f15-4f0c-ab4b-276cd736c67b', 'IN_STOCK', '2021-11-11 01:42:06.685000') on conflict do nothing;
insert into product values ('fc496d63-a931-49ff-ba44-2f8f482cc79b', 'product4', 'descProduct4', null, 'b49394f3-0f15-4f0c-ab4b-276cd736c67b', 'IN_STOCK', '2021-11-11 01:42:06.685000') on conflict do nothing;
insert into product values ('9af3781e-bd51-4912-960d-c0a1cb94e90d', 'product5', 'descProduct5', null, 'b49394f3-0f15-4f0c-ab4b-276cd736c67b', 'IN_STOCK', '2021-11-11 01:42:06.685000') on conflict do nothing;
insert into product values ('ab65501b-6b15-4bdc-a660-fac35eda39d9', 'product6', 'descProduct6', null, 'b49394f3-0f15-4f0c-ab4b-276cd736c67b', 'IN_STOCK', '2021-11-11 01:42:06.685000') on conflict do nothing;
insert into product values ('e523a867-6313-48f1-ab8f-6135fecf4b72', 'product7', 'descProduct7', null, 'b49394f3-0f15-4f0c-ab4b-276cd736c67b', 'IN_STOCK', '2021-11-11 01:42:06.685000') on conflict do nothing;

insert into product_price values ('a976b652-471b-4a36-b4c4-f91c3d6dae7b', '100.2', 'en_US', '1071989f-888e-4e3b-985d-6d9151a24619') on conflict do nothing;
insert into product_price values ('65356ee0-5aaa-4405-b0eb-fd1198ffc1e8', '12.23', 'en_US', '213f4e74-2ffe-4b5d-ac67-a3a56c1bdcf6') on conflict do nothing;
insert into product_price values ('e3b2d54c-7116-4b37-8761-c57fbfb0ff78', '327.435', 'en_US', 'a6a8eaba-dbde-488f-989b-7ed81b885c2d') on conflict do nothing;
insert into product_price values ('23983468-e07d-46d4-8e39-e8f8851087ef', '37.3', 'en_US', 'fc496d63-a931-49ff-ba44-2f8f482cc79b') on conflict do nothing;
insert into product_price values ('523fd48d-76e1-4071-a105-e2e0f7cadd3e', '2384.3343', 'en_US', '9af3781e-bd51-4912-960d-c0a1cb94e90d') on conflict do nothing;
insert into product_price values ('3c706a9b-4f93-40c2-9691-f1e765db0e30', '234', 'en_US', 'ab65501b-6b15-4bdc-a660-fac35eda39d9') on conflict do nothing;
insert into product_price values ('84de7207-7d05-4ccb-a714-490af880eff5', '65.6', 'en_US', 'e523a867-6313-48f1-ab8f-6135fecf4b72') on conflict do nothing;

insert into discount values ('87986f48-3ff8-4567-9510-574dbd1e291a', '34395038252394.34', '2021-11-01 01:42:06.685000', '2021-11-02 01:42:06.685000', 'a976b652-471b-4a36-b4c4-f91c3d6dae7b') on conflict do nothing;
insert into discount values ('64de877e-cfe9-4ae7-b311-b9e68af74b3a', '23433849.34', '2021-11-01 01:42:06.685000', '2022-11-01 01:42:06.685000', 'e3b2d54c-7116-4b37-8761-c57fbfb0ff78') on conflict do nothing;

insert into category values ('ef918665-f401-44cd-b347-21a3fb85cdcc', 'category1') on conflict do nothing;
insert into category values ('517d5af6-d4e2-4b89-9351-fd5811010723', 'category2') on conflict do nothing;
insert into category values ('7d0d0497-5432-44b7-b96b-ee38dd2095c8', 'category3') on conflict do nothing;

insert into product_category values ('ef918665-f401-44cd-b347-21a3fb85cdcc', '1071989f-888e-4e3b-985d-6d9151a24619') on conflict do nothing;
insert into product_category values ('7d0d0497-5432-44b7-b96b-ee38dd2095c8', '1071989f-888e-4e3b-985d-6d9151a24619') on conflict do nothing;
insert into product_category values ('517d5af6-d4e2-4b89-9351-fd5811010723', '213f4e74-2ffe-4b5d-ac67-a3a56c1bdcf6') on conflict do nothing;
insert into product_category values ('ef918665-f401-44cd-b347-21a3fb85cdcc', 'a6a8eaba-dbde-488f-989b-7ed81b885c2d') on conflict do nothing;
insert into product_category values ('517d5af6-d4e2-4b89-9351-fd5811010723', 'fc496d63-a931-49ff-ba44-2f8f482cc79b') on conflict do nothing;
insert into product_category values ('517d5af6-d4e2-4b89-9351-fd5811010723', '9af3781e-bd51-4912-960d-c0a1cb94e90d') on conflict do nothing;
insert into product_category values ('ef918665-f401-44cd-b347-21a3fb85cdcc', 'ab65501b-6b15-4bdc-a660-fac35eda39d9') on conflict do nothing;
insert into product_category values ('7d0d0497-5432-44b7-b96b-ee38dd2095c8', 'e523a867-6313-48f1-ab8f-6135fecf4b72') on conflict do nothing;



