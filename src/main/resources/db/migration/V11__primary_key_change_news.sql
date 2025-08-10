alter table company_news add column news_id int not null;
alter table company_news drop primary key;
alter table company_news drop column id;
alter table company_news add column id int not null auto_increment primary key;