ALTER TABLE portfolio
ADD CONSTRAINT fk_portfolio_figi
FOREIGN KEY (figi)
REFERENCES stock(figi);

ALTER TABLE portfolio
ADD CONSTRAINT fk_portfolio_quote
FOREIGN KEY (symbol, purchase_date)
REFERENCES quote(symbol, creation_date);