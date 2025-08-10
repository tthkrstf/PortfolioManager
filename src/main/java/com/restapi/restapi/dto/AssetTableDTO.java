package com.restapi.restapi.dto;

import lombok.Data;

@Data
public class AssetTableDTO {
    private String company;
    private String symbol;
    private double currentPrice;
    private double shares;
    private double totalWorth;
    private double paidAmount;
    private double profit;
    private double netWorth;
}
