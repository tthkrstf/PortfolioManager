package com.restapi.restapi.dto.external.finnhub;

import lombok.Data;

@Data
public class FinnhubQuoteRaw {
    private double c;   // current price
    private double d;   // change
    private double dp;  // percent change
    private double h;   // high
    private double l;   // low
    private double o;   // open
    private double pc;  // previous close
}