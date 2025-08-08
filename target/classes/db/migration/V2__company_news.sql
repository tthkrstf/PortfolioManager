CREATE TABLE company_news
(
    id int primary key not null,
    category varchar(50) not null,
    related varchar(10) not null,
    datetime date not null,
    headline text not null,
    image text,
    source text,
    summary text not null,
    url varchar(200)
);