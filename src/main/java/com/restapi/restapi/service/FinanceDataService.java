package com.restapi.restapi.service;

import com.restapi.restapi.client.FinanceDataProvider;
import com.restapi.restapi.dto.CompanyNewsDTO;
import com.restapi.restapi.dto.QuoteDTO;
import com.restapi.restapi.dto.external.finnhub.CompanyNewsRaw;
import com.restapi.restapi.dto.external.finnhub.QuoteRaw;
import com.restapi.restapi.mapper.FinnhubMapper;
import com.restapi.restapi.model.Quote;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceDataService {

    private final FinanceDataProvider client;
    private final FinnhubMapper finnhubMapper;
    private final JdbcTemplate jdbcTemplate;

    public FinanceDataService(@Qualifier("finnhubClient") final FinanceDataProvider client,
                              FinnhubMapper finnhubMapper, JdbcTemplate jdbcTemplate) {
        this.client = client;
        this.finnhubMapper = finnhubMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public QuoteDTO getQuote(String symbol) {
        QuoteRaw raw = client.getQuoteRaw(symbol);
        return finnhubMapper.mapQuote(raw, symbol);
    }

    public List<Quote> getAllQuotes(){
        List<Quote> quotes = jdbcTemplate.query("select * from quote",
                new Object[]{}, new BeanPropertyRowMapper<>(Quote.class));

        return quotes;
    }

    // POST
    public boolean createQuote(String symbol){
        QuoteDTO quoteDTO = this.getQuote(symbol);

            String sql = "INSERT INTO quote values(null, ?, ?, ?, ?, " +
                "?, ?, ?, ?, CURDATE(), null)";
            int rows = jdbcTemplate.update(sql, quoteDTO.getSymbol(), quoteDTO.getCurrentPrice(),
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

        String sql = "UPDATE quote set currentPrice = ?, changes = ?, percent_change = ?, high_price_of_day = ?, low_price_of_day = ?, open_price_of_day = ?," +
                "prev_close_price = ?, creation_date = CURDATE() where symbol = ?";
        int rows = jdbcTemplate.update(sql, existing.getCurrentPrice(), existing.getChanges(),
                existing.getPercentChange(), existing.getHighPriceOfDay(), existing.getLowPriceOfDay(), existing.getOpenPriceOfDay(),
                existing.getPrevClosePrice(), symbol);

        return rows == 1;
    }

    //DELETE
    public boolean deleteQuote(String symbol){
        String sql = "DELETE FROM quote where symbol = ?";
        int rows = jdbcTemplate.update(sql, symbol);
        return rows == 1;
    }

    public List<CompanyNewsDTO> getCompanyNews(String symbol){
        List<? extends CompanyNewsRaw> companyNewsRaws = client.getCompanyNewsRaw(symbol);
        return finnhubMapper.mapNews(companyNewsRaws, symbol);
    }
}
