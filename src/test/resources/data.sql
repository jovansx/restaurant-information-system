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
('2020-12-15T00:00:00.000', 2100);

insert into ROLE (name) values
('MANAGER'),
('SYSTEM_ADMIN'),
('ADMIN');

insert into unregistered_user (first_name, last_name, email_address, phone_number, salary_id, type, is_deleted, pin_code) values
('John','Cena','johncena@gmail.com','0611111111', 1, 'WAITER', false, '1111'),
('Simon','Baker','simonbaker@gmail.com','0611111111', 2, 'BARTENDER', false, '1112'),
('Elon','Musk','elonmusk@gmail.com','0611111111', 1, 'CHEF', false, '1113');
