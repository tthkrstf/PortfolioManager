package com.restapi.restapi.client;

import com.restapi.restapi.dto.external.finnhub.CompanyNewsRaw;
import com.restapi.restapi.dto.external.finnhub.QuoteRaw;

import java.util.List;

public interface FinanceDataProvider {

     public QuoteRaw getQuoteRaw(String symbol);
     public List<? extends CompanyNewsRaw> getCompanyNewsRaw(String symbol);
}
