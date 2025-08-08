package com.restapi.restapi.dto;

import com.restapi.restapi.common.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PortfolioDTO {
    private String symbol; // Stock symbol
    private double shares; // Number of shares purchased
    private double pricePerShare; // Price at the time of buying
    private String currency; // Currency
    private Transaction type; // Currency
}
