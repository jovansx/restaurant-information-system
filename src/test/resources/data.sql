insert into salary (created_at, value) values
('2021-01-01T00:00:00.000', 1000),
('2021-01-01T00:00:00.000', 2000),
('2021-02-01T00:00:00.000', 2500),
('2021-03-08T00:00:00.000', 2800),
('2021-03-08T01:00:00.000', 2100),
('2021-03-13T03:00:00.000', 2100),
('2021-03-28T02:00:00.000', 2100),
('2021-07-01T00:00:00.000', 2100),
('2021-10-01T00:00:00.000', 2100),
('2021-07-01T00:00:00.000', 1900),
('2020-12-15T00:00:00.000', 2100),
('2020-12-16T00:00:00.000', 2400);

insert into role (name) values
('MANAGER'),
('SYSTEM_ADMIN'),
('ADMIN');

insert into my_user (first_name, last_name, email_address, phone_number, type, is_deleted) values
('John','Cena','johncena@gmail.com','0611111111', 3, false),
('Simon','Baker','simonbaker@gmail.com','0611111112', 4, false),
('Elon','Musk','elonmusk@gmail.com','0611111113', 5, false),
('Chili','Kalibrk','chilikalibrk@gmail.com','0611111114', 3, false),
('Simon','Cowel','simoncowel@gmail.com','0611111115', 4, false),
('Eloner','Muskila','elonermuskila@gmail.com','0611111116', 5, false),
('Calikikoki','Garibai','calikikoki@gmail.com','0611111117', 3, false),
('Kalionear','Calioki','kalionear@gmail.com','0611111118', 3, false),
('Brad','Pitt','bradpitt@gmail.com','0611111119', 1, false),
('Michael','Douglas','michaeldouglas@gmail.com','0611111110', 0, false),
('Liam','Neeson','liamneeson@gmail.com','0611111122', 2, false);

insert into unregistered_user (id, pin_code) values
(1, '1111'),
(2, '1112'),
(3, '1113'),
(4, '1114'),
(5, '1115'),
(6, '1116'),
(7, '1117'),
(8, '1118');

insert into registered_user (id, username, password, role_id) values
(9, 'bradpitt', '$2a$04$DbLOb2nXmJyS4cryCilJC.G1xlMYVoKNg0KSyGgGv/QswcfLnTTvq', 1),
(10, 'michaeldouglas', '$2a$04$7yyD1PQZkTgZ4gr14l34zu/Pblf0Zde.Si1OaugvF/bTJ05fehdOC', 3),
(11, 'liamneeson', '$2a$04$DW.8hGuG2saGv1srE/DLKuTgwjkcea6jMOqjjaTym/ufxnSihDU66', 2);

insert into my_user_salary (user_id, salary_id)  values
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10),
(11, 11),
(11, 12);

insert into item_category (name) values
('Juices'),
('Cocktails'),
('Sandwich'),
('Meat'),
('Soup');

insert into item (code, description, icon_base_64, name, original, type, item_category_id, deleted) values
('c6d7d3c8-2273-4343-a6dc-87efe43867fa', 'Very good apple juice!', null,'Apple juice', true, 1, 1, false),
('a324175f-742d-4211-93ea-0b5280412306', 'Very good orange juice!',  null, 'Orange juice', true, 1, 1, false),
('a8b9aab8-f7dc-4966-a3eb-09eecb7fa9d9', 'Very good cocktail!', null, 'Sex on the beach', true, 1, 2, false),
('7807ec36-1888-44a9-8fc5-ca11df02492f', 'Very good chicken sandwich!', null, 'Chicken sandwich', true, 0, 3, false),
('9a191868-228d-4dbb-819f-ca615d29fefe', 'Very good chicken breast', null, 'Chicken breast', true, 0, 4, false),
('9a191868-228d-4dbb-819f-ca615d29fefe', 'Solid chicken breast', null, 'Chicken breast', false, 0, 4, false),
('7807ec36-1888-44a9-8fc5-ca11df02492f', 'Bad chicken sandwich!', null, 'Chicken sandwich', false, 0, 3, true);

insert into item_components (item_id, components) values
(1, 'Apple'),
(1, 'Sugar'),
(2, 'Orange'),
(2, 'Sugar'),
(3, 'Potato'),
(3, 'Sugar'),
(4, 'Vodka'),
(4, 'Chicken'),
(4, 'Tomato'),
(4, 'Bread'),
(5, 'Chicken'),
(5, 'Potato'),
(6, 'Chicken'),
(6, 'Potato');

insert into price (created_at, value) values
('2021-11-03T00:00:00.000', 5),
('2021-11-04T00:00:00.000', 8),
('2021-11-01T00:00:00.000', 7),
('2021-11-02T00:00:00.000', 10),
('2021-05-02T00:00:00.000', 150),
('2020-04-03T00:00:00.000', 100),
('2019-04-03T00:00:00.000', 100),
('2019-04-04T00:00:00.000', 200);

insert into item_prices (item_id, prices_id) values
(1, 1),
(1, 2),
(2, 3),
(3, 4),
(4, 5),
(4, 6),
(5, 7),
(6, 8);

insert into my_order_item (active, created_at, deleted, notes, state) values
(true, '2021-11-30T10:05:00.000', false, null, 1),
(true, '2021-11-30T09:55:00.000', false, 'Grill it a little bit longer!', 2),
(true, '2021-11-30T09:45:00.000', false, null, 3),
(true, '2021-11-30T10:00:00.000', false, 'He wants good apple joice!', 4),
(true, '2021-11-30T10:05:00.000', false, null, 2),
(true, '2021-11-30T10:06:00.000', false, null, 1),
(true, '2021-11-30T11:00:00.000', false, null, 1),
(true, '2021-11-30T11:00:00.000', false, null, 3),
(true, '2021-11-30T11:00:00.000', false, null, 4);

insert into dish_item (id, amount, chef_id, item_id) values
(1, 1, null, 4),
(2, 3, 3, 5),
(3, 1, 3, 5),
(4, 10, 3, 5);

insert into drink_item (amount, item_id) values
(1, 1),
(2, 2),
(3, 1),
(2, 3),
(1, 3),
(1, 3);

insert into drink_items (id, bartender_id) values
(5, 2),
(6, null),
(7, null),
(8, 2),
(9, null);

insert into drink_items_drink_item_list (drink_items_id, drink_item_list_id) values
(5, 1),
(5, 2),
(6, 3),
(7, 4),
(8, 5),
(9, 6);

insert into my_order (active, created_at, discarded, total_price, waiter_id) values
(true, '2021-01-31T00:00:00.000', false, 8, 1),
(true, '2021-01-01T02:00:00.000', false, 1100, 1),
(true, '2021-02-01T02:02:00.000', false, 1000, 1),
(false, '2021-03-01T02:02:00.000', true, 1200, 1),
(false, '2021-03-10T02:00:00.000', false, 1200, 1),
(true, '2021-03-31T02:00:00.000', false, 1200, 1),
(true, '2021-05-01T02:00:00.000', false, 1200, 1),
(true, '2021-05-20T02:00:00.000', false, 1200, 1),
(true, '2021-05-15T02:00:00.000', false, 1200, 1),
(true, '2020-12-10T02:00:00.000', false, 1200, 1);

insert into my_order_dishes (order_id, dishes_id) values
(1, 1),
(1, 2),
(1, 3),
(1, 4);

insert into my_order_drinks (order_id, drinks_id) values
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9);

insert into restaurant_table (is_deleted, name, shape, state, order_id) values
(false, 'T1', 0, 1, 1),
(false, 'T2', 0, 0, null);

insert into room (is_deleted, name) values
(false, 'Room 1');

insert into room_restaurant_tables (room_id, restaurant_tables_id) values
(1, 1);