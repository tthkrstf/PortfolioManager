package com.restapi.restapi.service;

import com.restapi.restapi.client.FinanceDataProvider;
import com.restapi.restapi.dto.PasswordsDTO;
import com.restapi.restapi.dto.QuoteDTO;
import com.restapi.restapi.dto.external.finnhub.FinnhubQuoteRaw;
import com.restapi.restapi.mapper.FinnhubQuoteMapper;
import com.restapi.restapi.model.Passwords;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<QuoteDTO> getAllQuotes(){
        List<QuoteDTO> quotes = jdbcTemplate.query("select * from Quote",
                new Object[]{}, new BeanPropertyRowMapper<>(QuoteDTO.class));

        return quotes;
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

        //PUT
    public boolean putQuote(QuoteDTO quoteDTO, final String symbol){
        QuoteDTO existing = getQuote(symbol);

        if(existing == null){
            return false;
        }

        if(quoteDTO.getCurrentPrice() != 0) existing.setCurrentPrice(quoteDTO.getCurrentPrice());
        if(quoteDTO.getChanges() != 0) existing.setChanges(quoteDTO.getChanges());
        if(quoteDTO.getPercentChange() != 0) existing.setPercentChange(quoteDTO.getCurrentPrice());
        if(quoteDTO.getHighPriceOfDay() != 0) existing.setHighPriceOfDay(quoteDTO.getHighPriceOfDay());
        if(quoteDTO.getLowPriceOfDay() != 0) existing.setLowPriceOfDay(quoteDTO.getLowPriceOfDay());
        if(quoteDTO.getOpenPriceOfDay() != 0) existing.setOpenPriceOfDay(quoteDTO.getOpenPriceOfDay());
        if(quoteDTO.getPrevClosePrice() != 0) existing.setPrevClosePrice(quoteDTO.getPrevClosePrice());

        String sql = "UPDATE Quote set currentPrice = ?, changes = ?, percentChange = ?, url = ?, notes = ?, modificationdate = CURDATE() where symbol = ?";
        int rows = jdbcTemplate.update(sql, existing.getName(), existing.getPassword(),
                existing.getUrl(), existing.getNotes(), id);

        return rows == 1;
    }

    //DELETE
    public boolean deleteQuote(String symbol){
        String sql = "DELETE FROM Quote where symbol = ?";
        int rows = jdbcTemplate.update(sql, symbol);
        return rows == 1;
    }
}
