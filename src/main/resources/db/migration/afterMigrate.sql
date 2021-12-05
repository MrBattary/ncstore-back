--default and essential database data should be here
insert into role values ('0d82249f-b1d7-48a6-8818-dc22752903ed', 'ADMIN') on conflict do nothing ;
insert into role values ('1d186379-cfff-4c30-8803-482ae94a9e79', 'SUPPLIER') on conflict do nothing ;
insert into role values ('24cccce3-67a4-4267-a910-537eba38a5cb', 'CUSTOMER') on conflict do nothing ;

insert into price_conversion_rate values ('42cd6a0d-a369-40c0-8d02-381378154a58', 'en_US', 1.0) on conflict do nothing;

--test database data should be here (can be deleted any time)

INSERT INTO category values ('d894a390-c35b-4fb0-b8b4-def80173e321', 'Design') on conflict do nothing;
INSERT INTO category values ('ea010061-b34c-4e1b-954a-bdf07ae9fc2d', 'Office') on conflict do nothing;
INSERT INTO category values ('6936bbb8-ad34-48fa-a983-2a44fcb61075', 'Finance') on conflict do nothing;
INSERT INTO category values ('084dcdb6-fe52-4b74-946c-fef72f041809', 'Entertainment') on conflict do nothing;
INSERT INTO category values ('5ea6571e-676b-424f-aa29-1ba0684b4f4e', 'Education') on conflict do nothing;
INSERT INTO category values ('aac5788b-b02d-4dcf-b873-0d2033bece2a', 'Lifestyle') on conflict do nothing;
INSERT INTO category values ('8d282283-5946-4984-a910-727615d31513', 'Music') on conflict do nothing;
INSERT INTO category values ('cfa21e98-4bd9-4d07-a47b-29013bed4323', 'Networking') on conflict do nothing;
INSERT INTO category values ('f9a56b5f-e996-4d84-a888-b4eb7a2f022f', 'Operating Systems') on conflict do nothing;
INSERT INTO category values ('95a6ccee-1cf1-4975-9d6e-f4d45a261cdd', 'Programming') on conflict do nothing;
INSERT INTO category values ('9ce32f32-daeb-478d-b1f5-0e5e9a87dbf7', 'Utilities') on conflict do nothing;
INSERT INTO category values ('ec98ae3c-fa69-45c5-a87e-79aa38a45a3d', 'Video') on conflict do nothing;
INSERT INTO category values ('39c6cc8f-2135-44bc-aa71-ab150a83e704', 'Security') on conflict do nothing;
INSERT INTO category values ('6857a441-b8e6-4793-a2ac-12eb25d27b29', 'Business') on conflict do nothing;
INSERT INTO category values ('7e94fd3c-dc53-4127-81d8-b27c574b583f', 'Accounting') on conflict do nothing;
INSERT INTO category values ('b3743453-8545-409f-ba35-a76baee09fbc', 'Other') on conflict do nothing;