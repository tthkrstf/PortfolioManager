package com.restapi.restapi.client;

import com.restapi.restapi.dto.external.finnhub.FinnhubQuoteRaw;

public interface FinanceDataProvider {

     public FinnhubQuoteRaw getQuoteRaw(String symbol);

}
