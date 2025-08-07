package com.restapi.restapi.dto;

import lombok.Data;

@Data
public class StockDTO {
    private String figi; // FIGI identifier.
    private String symbol; // Display symbol name.
    private String displaySymbol; // Display symbol name.
    private String description; // Symbol description
    private String mic; // Primary exchange's MIC.
    private String currency; // Price's currency.
    private String type; // Security type.

    public StockDTO(String figi, String symbol, String displaySymbol, String description, String mic, String currency, String type) {
        this.figi = figi;
        this.symbol = symbol;
        this.displaySymbol = displaySymbol;
        this.description = description;
        this.mic = mic;
        this.currency = currency;
        this.type = type;
    }
}
