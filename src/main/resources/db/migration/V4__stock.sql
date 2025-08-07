CREATE TABLE stock (
     figi VARCHAR(20) primary key not null,
     currency VARCHAR(10),
     description VARCHAR(255),
     displaySymbol VARCHAR(20),
     mic VARCHAR(10),
     symbol VARCHAR(20),
     type VARCHAR(50)
);