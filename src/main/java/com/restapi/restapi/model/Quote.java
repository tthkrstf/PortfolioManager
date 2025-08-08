package com.restapi.restapi.model;

import lombok.Data;

import java.util.Date;

@Data
public class Quote {
    private String symbol;
    private double currentPrice;
    private double changes;
    private double percentChange;
    private double highPriceOfDay;
    private double lowPriceOfDay;
    private double openPriceOfDay;
    private double prevClosePrice;
    public Date creationDate;
}
