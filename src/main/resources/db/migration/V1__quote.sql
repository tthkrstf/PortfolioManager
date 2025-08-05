CREATE TABLE quote
(
   id int primary key not null auto_increment,
   symbol varchar(10) not null,
   currentPrice dec(15,2) not null,
   changes dec(15,2) not null,
   percent_change dec(15,2) not null,
   high_price_of_day dec(15,2) not null,
   low_price_of_day dec(15,2) not null,
   open_price_of_day dec(15,2) not null,
   prev_close_price dec(15,2) not null,
   creation_date date not null,
   modification_date date null
);