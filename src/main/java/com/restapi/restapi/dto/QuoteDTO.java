package com.restapi.restapi.dto;

import com.restapi.restapi.model.Quote;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuoteDTO {
    private String symbol; // stock
    private double currentPrice; // current price
    private double changes; // change
    private double percentChange; // percent change
    private double highPriceOfDay; // high
    private double lowPriceOfDay; // low
    private double openPriceOfDay; // open
    private double prevClosePrice; // previous close

    public QuoteDTO(Quote quote) {
        this.symbol = quote.getSymbol();
        this.currentPrice = quote.getCurrentPrice();
        this.changes = quote.getChanges();
        this.percentChange = quote.getPercentChange();
        this.highPriceOfDay = quote.getHighPriceOfDay();
        this.lowPriceOfDay = quote.getLowPriceOfDay();
        this.openPriceOfDay = quote.getOpenPriceOfDay();
        this.prevClosePrice = quote.getPrevClosePrice();
    }
}
