package com.restapi.restapi.dto;

import lombok.Data;

@Data
public class QuoteDTO {
    private String symbol;
    private double currentPrice;
    private double changes;
    private double percentChange;
    private double highPriceOfDay;
    private double lowPriceOfDay;
    private double openPriceOfDay;
    private double prevClosePrice;

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
