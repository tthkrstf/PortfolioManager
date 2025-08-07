package com.restapi.restapi.model;

import lombok.Data;

import java.util.Date;

@Data
public class Portfolio {
    private int id; // id
    private String symbol; // Stock symbol
    private double pricePerShare; // Price per share at purchase
    private double shares; // Number of shares purchased
    private double totalPrice; // Total price paid = pricePerShare * shares
    private Date purchaseDate; // Purchase date
    private String figi; // Figi ID number for a stock
}
