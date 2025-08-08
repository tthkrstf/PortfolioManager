package com.restapi.restapi.model;

import com.restapi.restapi.common.Transaction;
import lombok.Data;

import java.util.Date;

@Data
public class Portfolio {
    private int id; // id
    private String symbol; // Stock symbol
    private double pricePerShare; // Price per share at purchase
    private double shares; // Number of shares purchased
    private Date purchaseDate; // Purchase date
    private String currency; // Purchase date
    private Transaction type; // Buy or Sell
}

