package com.restapi.restapi.model;

import java.util.Date;

public class Quote {
    private int id;
    private String symbol;
    private double currentPrice;
    private double changes;
    private double percentChange;
    private double highPriceOfDay;
    private double lowPriceOfDay;
    private double openPriceOfDay;
    private double prevClosePrice;
    public Date creationDate;
    public Date modificationDate;
}
