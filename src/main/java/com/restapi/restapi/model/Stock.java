package com.restapi.restapi.model;

import lombok.Data;

@Data
public class Stock {
    private String figi; // FIGI identifier.
    private String symbol; // Display symbol name.
    private String displaySymbol; // Display symbol name.
    private String description; // Symbol description
    private String mic; // Primary exchange's MIC.
    private String currency; // Price's currency.
    private String type; // Security type.
}
