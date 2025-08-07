package com.restapi.restapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PortfolioDTO {
    private String symbol; // Stock symbol
    private double shares; // Number of shares purchased
    private double totalPrice; // Total price paid = pricePerShare * shares
    private Date purchaseDate; // Purchase date

    public PortfolioDTO(String symbol, double shares, double totalPrice, Date purchaseDate) {
        this.symbol = symbol;
        this.shares = shares;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
    }
}
