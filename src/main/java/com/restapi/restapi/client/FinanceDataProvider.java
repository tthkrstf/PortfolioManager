package com.restapi.restapi.client;

import com.restapi.restapi.dto.external.CompanyNewsRaw;
import com.restapi.restapi.dto.external.QuoteRaw;
import com.restapi.restapi.dto.external.StockRaw;

import java.util.List;

public interface FinanceDataProvider {

     public QuoteRaw getQuoteRaw(String symbol);
     public List<? extends CompanyNewsRaw> getCompanyNewsRaw(String symbol);
     public List<? extends StockRaw> getStocksRaw();
}
