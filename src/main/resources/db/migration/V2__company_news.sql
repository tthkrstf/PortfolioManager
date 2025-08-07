CREATE TABLE company_news
(
    id int primary key not null,
    related varchar(10) not null,
    datetime date not null,
    headline varchar(50) not null,
    image varchar(500),
    source varchar(500),
    summary text not null,
    url varchar(200)
);