# API Endpoints

## **POST Endpoints**

| Endpoint        | Description                                                                                         | Parameters |
|-----------------|-----------------------------------------------------------------------------------------------------|-------------------|
| **`/stock`**    | Fetches all stock symbols from Finnhub and saves them to the DB. Runs automatically if needed.      | None            |
| **`/quote`**    | Fetches one symbol's quote data for the current day from Finnhub and saves it to the DB.             | `{ "symbol": "TSLA" }` |
| **`/portfolio`**| Saves a stock transaction (e.g., buying shares) into the DB.                                        | `{ "symbol": "TSLA", "shares": 5000.0, "pricePerShare": 0.1, "currency": "USD", "type": "BUY" }` |
| **`/company_news`** | Fetches one symbol's company news for the current and previous day from Finnhub and saves it to the DB. | `{ "symbol": "TSLA" }` |

---

## **GET Endpoints**

| Endpoint                  | Description                                                                     | Parameters |
|---------------------------|---------------------------------------------------------------------------------|------------|
| **`/portfolio`**          | Returns the portfolio with total share amounts for all symbols.                 | None     |
| **`/quotes`**             | Returns all quotes for a given date.                                            | `date=2025-08-08` |
| **`/quotes/{symbol}`**      | Returns a given symbol’s quote for a given date.                                | `date=2025-08-08`<br>`symbol=TSLA` |
| **`/company_news/{symbol}`** | Returns a given symbol’s company news for the current and previous day.         | `symbol=TSLA` |

## **DELETE Endpoints**

| Endpoint        | Description                                                                                                                             | Parameters (JSON) |
|-----------------|-----------------------------------------------------------------------------------------------------------------------------------------|-------------------|
| **`/portfolio`**| Sells shares from the given stock in the specified amount. If the amount exceeds the current available shares, no selling occurs.       | `{ "symbol": "TSLA", "shares": 0.1 }` |
