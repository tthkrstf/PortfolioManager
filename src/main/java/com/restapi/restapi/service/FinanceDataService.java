package com.restapi.restapi.service;

import com.restapi.restapi.client.FinanceDataProvider;
import com.restapi.restapi.common.Transaction;
import com.restapi.restapi.dto.*;
import com.restapi.restapi.dto.external.CompanyNewsRaw;
import com.restapi.restapi.dto.external.QuoteRaw;
import com.restapi.restapi.dto.external.StockRaw;
import com.restapi.restapi.mapper.FinnhubMapper;
import com.restapi.restapi.model.CompanyNews;
import com.restapi.restapi.model.Quote;
import com.restapi.restapi.model.Stock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.sql.Date;
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

    // TO DB
    public QuoteDTO getQuote(String symbol) {
        QuoteRaw raw = client.getQuoteRaw(symbol);
        return finnhubMapper.mapQuote(raw, symbol);
    }

    // FROM DB
    public QuoteDTO getQuoteFromDB(String symbol, LocalDate date) {
        try{
            return fetchQuote(symbol, date);
        } catch(EmptyResultDataAccessException e){
            createQuote(symbol);

            try{
                return fetchQuote(symbol, date);
            } catch(EmptyResultDataAccessException ex){
                return null;
            }

        }
    }

    // FROM DB
    private QuoteDTO fetchQuote(String symbol, LocalDate date){
        Quote quote = jdbcTemplate.queryForObject("select * from quote where symbol = ? and creation_date = ?",
                new Object[]{symbol, date}, new BeanPropertyRowMapper<>(Quote.class));

        return new QuoteDTO(quote);
    }

    // FROM DB
    public List<QuoteDTO> getAllQuotes(LocalDate date){
        Date sqlDate = Date.valueOf(date);
        List<Quote> quotes = jdbcTemplate.query("select * from quote where creation_date = ?",
                new Object[]{sqlDate}, new BeanPropertyRowMapper<>(Quote.class));

        return finnhubMapper.mapQuote(quotes);
    }

    // POST
    public boolean createQuote(String symbol){
        QuoteDTO quoteDTO = this.getQuote(symbol);

        String sql = "INSERT IGNORE INTO quote values(?, ?, ?, ?, " +
            "?, ?, ?, ?, CURDATE())";
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

    public List<CompanyNewsDTO> getCompanyNewsFromDB(String symbol){
        try{
            List<? extends CompanyNews> companyNews = jdbcTemplate.query("select * from company_news where related = ?",
                    new Object[]{symbol}, new BeanPropertyRowMapper<>(CompanyNews.class));

            return finnhubMapper.mapNews(companyNews);
        } catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Transactional
    public boolean createCompanyNews(String symbol){
        List<CompanyNewsDTO> companyNewsDTO = this.getCompanyNews(symbol);

        String sql = "INSERT IGNORE INTO company_news values(?, ?, ?, ?, ?, " +
                "?, ?, ?, ?)";
        int[] rows = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CompanyNewsDTO news = companyNewsDTO.get(i);
                ps.setInt(1, news.getId());
                ps.setString(2, news.getCategory());
                ps.setString(3, news.getRelated());
                ps.setDate(4, news.getDatetime());
                ps.setString(5, news.getHeadline());
                ps.setString(6, news.getImage());
                ps.setString(7, news.getSource());
                ps.setString(8, news.getSummary());
                ps.setString(9, news.getUrl());
            }

            @Override
            public int getBatchSize() {
                return companyNewsDTO.size();
            }
        });
        return Arrays.stream(rows).sum() > 0;
    }

    public List<StockDTO> getStocks(){
        List<? extends StockRaw> stockRaws = client.getStocksRaw();
        return finnhubMapper.mapStocks(stockRaws);
    }

    @Transactional
    public boolean createStock() {
        List<StockDTO> stockDTO = this.getStocks();

        String sql = "INSERT IGNORE INTO stock VALUES (?, ?, ?, ?, ?, ?, ?)";
        int[] rows =  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, stockDTO.get(i).getFigi());
                ps.setString(2, stockDTO.get(i).getCurrency());
                ps.setString(3, stockDTO.get(i).getDescription());
                ps.setString(4, stockDTO.get(i).getDisplaySymbol());
                ps.setString(5, stockDTO.get(i).getMic());
                ps.setString(6, stockDTO.get(i).getSymbol());
                ps.setString(7, stockDTO.get(i).getType());
            }

            @Override
            public int getBatchSize() {
                return stockDTO.size();
            }
        });

        return Arrays.stream(rows).sum() > 0;
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocks = jdbcTemplate.query("select * from stock",
                new Object[]{}, new BeanPropertyRowMapper<>(Stock.class));

        return stocks;
    }

    public List<CompanyNews> getAllCompanyNews() {
        List<CompanyNews> companyNews = jdbcTemplate.query("select * from company_news",
                new Object[]{}, new BeanPropertyRowMapper<>(CompanyNews.class));

        return companyNews;
    }

    public StockDTO getStockBySymbolFromDB(String symbol) {
        try{
            return fetchStock(symbol);
        } catch(EmptyResultDataAccessException e){
            createStock();

            try{
                return fetchStock(symbol);
            } catch(EmptyResultDataAccessException ex){
                return null;
            }
        }
    }

    private StockDTO fetchStock(String symbol){
        String sql = "select * from stock where symbol = ?";
        Stock stock = jdbcTemplate.queryForObject(sql, new Object[]{symbol}, new BeanPropertyRowMapper<>(Stock.class));

        return new StockDTO(stock);
    }

    public boolean createPortfolio(PortfolioDTO portfolioDTO){
        QuoteDTO quote = getQuoteFromDB(portfolioDTO.getSymbol(), LocalDate.now());
        StockDTO stockDTO = getStockBySymbolFromDB(portfolioDTO.getSymbol());

        double currentPrice = quote.getCurrentPrice();
        double shares = portfolioDTO.getShares();

        String sql = "INSERT INTO portfolio values(null, ?, ?, ?, ?, ?, ?)";
        int rows = jdbcTemplate.update(sql, portfolioDTO.getSymbol(), currentPrice,
                shares, portfolioDTO.getPurchaseDate(), stockDTO.getCurrency(), Transaction.BUY.toString()
        );
        return rows == 1;
    }

    public List<SharesDTO> getAllFromPortfolio() {
        String sql = "select symbol, sum(case when type = 'BUY' then shares when type = 'SELL' then -shares else 0 end) as shares from portfolio group by symbol";
        List<SharesDTO> shares = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(SharesDTO.class));
        return shares.isEmpty() ? null : shares;
    }

    public Double getAmountForSymbol(String symbol) {
        String sql = "select sum(case when type = 'BUY' then shares when type = 'SELL' then -shares else 0 end) as shares from portfolio where symbol = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, symbol);
    }

    public boolean sellFromPortfolio(SharesDTO sharesDTO){
        if(sharesDTO.getShares() > getAmountForSymbol(sharesDTO.getSymbol())){
            return false;
        }

        QuoteDTO quote = getQuoteFromDB(sharesDTO.getSymbol(), LocalDate.now());
        StockDTO stockDTO = getStockBySymbolFromDB(sharesDTO.getSymbol());
        String sql = "INSERT INTO portfolio values(null, ?, ?, ?, CURDATE(), ?, ?)";
        int rows = jdbcTemplate.update(sql, sharesDTO.getSymbol(), quote.getCurrentPrice(),
                sharesDTO.getShares(), stockDTO.getCurrency(), Transaction.SELL.toString()
        );
        return rows == 1;
    }
}
