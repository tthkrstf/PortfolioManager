package com.restapi.restapi.service;

import com.restapi.restapi.client.FinanceDataProvider;
import com.restapi.restapi.dto.QuoteDTO;
import com.restapi.restapi.dto.external.finnhub.FinnhubQuoteRaw;
import com.restapi.restapi.mapper.FinnhubQuoteMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class FinanceDataService {

        private final FinanceDataProvider client;
        private final FinnhubQuoteMapper quoteMapper;
        private final JdbcTemplate jdbcTemplate;

        public FinanceDataService(@Qualifier("finnhubClient") final FinanceDataProvider client,
                                  FinnhubQuoteMapper quoteMapper, JdbcTemplate jdbcTemplate) {
            this.client = client;
            this.quoteMapper = quoteMapper;
            this.jdbcTemplate = jdbcTemplate;
        }

        public QuoteDTO getQuote(String symbol) {
            FinnhubQuoteRaw raw = client.getQuoteRaw(symbol);
            return quoteMapper.map(raw, symbol);
        }

        // POST
        public boolean createQuote(String symbol){
            QuoteDTO quoteDTO = this.getQuote(symbol);

            String sql = "INSERT INTO Quote values(null, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, CURDATE(), null)";
            int rows = jdbcTemplate.update(sql, quoteDTO.getSymbol(), quoteDTO.getPercentChange(),
                    quoteDTO.getChanges(), quoteDTO.getPercentChange(), quoteDTO.getHighPriceOfDay(),
                    quoteDTO.getLowPriceOfDay(), quoteDTO.getOpenPriceOfDay(), quoteDTO.getPrevClosePrice());
            return rows == 1;
        }
}
