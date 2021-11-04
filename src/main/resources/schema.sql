create table bids (bid_id bigint not null, bid_date date, bid_value double not null, item_id bigint, user_id bigint, primary key (bid_id));

create table items (item_id bigint not null, bid_increment double not null, buy_it_now boolean not null, description varchar(255), start_date date, start_price double not null, stop_date date, title varchar(255), user_id bigint, primary key (item_id));

create table users (user_id bigint not null, billing_address varchar(255), full_name varchar(255), login varchar(255), password varchar(255), primary key (user_id));

alter table bids add constraint FKg1mdb2uha9v6t2ujkvlmj3tuq foreign key (item_id) references items;

alter table bids add constraint FKmb21nl8gr3srgnlch3s18oqv9 foreign key (user_id) references users;

alter table items add constraint FKft8pmhndq1kntvyfaqcybhxvx foreign key (user_id) references users;
