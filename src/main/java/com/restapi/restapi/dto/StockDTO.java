package com.restapi.restapi.dto;

import com.restapi.restapi.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDTO {
    private String figi; // FIGI identifier.
    private String symbol; // Display symbol name.
    private String displaySymbol; // Display symbol name.
    private String description; // Symbol description
    private String mic; // Primary exchange's MIC.
    private String currency; // Price's currency.
    private String type; // Security type.

    public StockDTO(Stock stock) {
        this.figi = stock.getFigi();
        this.symbol = stock.getSymbol();
        this.displaySymbol = stock.getDisplaySymbol();
        this.description = stock.getDescription();
        this.mic = stock.getMic();
        this.currency = stock.getCurrency();
        this.type = stock.getType();
    }
}
