package com.restapi.restapi.mapper;

import com.restapi.restapi.dto.external.finnhub.FinnhubQuoteRaw;
import com.restapi.restapi.dto.QuoteDTO;
import org.springframework.stereotype.Component;

@Component
public class FinnhubQuoteMapper {
    public QuoteDTO map(FinnhubQuoteRaw raw, String symbol) {
        return new QuoteDTO(symbol, raw.getC(), raw.getD(), raw.getDp(),
                raw.getH(), raw.getL(), raw.getO(), raw.getPc());
    }
}
