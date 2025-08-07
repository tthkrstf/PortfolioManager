CREATE TABLE portfolio
(
    id int primary key not null auto_increment,
    symbol varchar(10) not null,
    price_per_share dec(15,2) not null,
    shares dec(15,2) not null,
    total_price dec(15,2) not null,
    purchase_date date not null,
    figi VARCHAR(20)
);