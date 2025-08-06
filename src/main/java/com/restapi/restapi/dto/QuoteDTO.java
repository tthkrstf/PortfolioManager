package com.restapi.restapi.dto;

import lombok.Data;

@Data
public class QuoteDTO {
    private String symbol; // stock
    private double currentPrice; // current price
    private double changes; // change
    private double percentChange; // percent change
    private double highPriceOfDay; // high
    private double lowPriceOfDay; // low
    private double openPriceOfDay; // open
    private double prevClosePrice; // previous close

    public QuoteDTO(String symbol, double currentPrice, double changes, double percentChange, double highPriceOfDay,
                    double lowPriceOfDay, double openPriceOfDay, double prevClosePrice) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.changes = changes;
        this.percentChange = percentChange;
        this.highPriceOfDay = highPriceOfDay;
        this.lowPriceOfDay = lowPriceOfDay;
        this.openPriceOfDay = openPriceOfDay;
        this.prevClosePrice = prevClosePrice;
    }
}
