--default and essential database data should be here
insert into role values ('0d82249f-b1d7-48a6-8818-dc22752903ed', 'ADMIN') on conflict do nothing ;
insert into role values ('1d186379-cfff-4c30-8803-482ae94a9e79', 'SUPPLIER') on conflict do nothing ;
insert into role values ('24cccce3-67a4-4267-a910-537eba38a5cb', 'CUSTOMER') on conflict do nothing ;

insert into price_conversion_rate values ('42cd6a0d-a369-40c0-8d02-381378154a58', 'en_US', 1.0) on conflict do nothing;

--test database data should be here (can be deleted any time)
insert into category values ('ef918665-f401-44cd-b347-21a3fb85cdcc', 'category1') on conflict do nothing;
insert into category values ('517d5af6-d4e2-4b89-9351-fd5811010723', 'category2') on conflict do nothing;
insert into category values ('7d0d0497-5432-44b7-b96b-ee38dd2095c8', 'category3') on conflict do nothing;