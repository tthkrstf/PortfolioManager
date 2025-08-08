package com.restapi.restapi.common;

public enum Transaction {
    BUY("Buy"),
    SELL("Sell");

    private final String value;

    Transaction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
