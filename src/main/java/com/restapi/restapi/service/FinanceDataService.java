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


import javax.sound.sampled.Port;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;
import java.util.stream.Collectors;

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
    public boolean putQuote(QuoteDTO quoteDTO){
        QuoteDTO existing = getQuote(quoteDTO.getSymbol());

        if(existing == null){
            return false;
        }

        if(quoteDTO.getCurrentPrice() != 0) existing.setCurrentPrice(quoteDTO.getCurrentPrice());

        String sql = "UPDATE quote set currentPrice = ? where symbol = ? and creation_date = CURDATE()";
        int rows = jdbcTemplate.update(sql, existing.getCurrentPrice(), quoteDTO.getSymbol());

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
        List<CompanyNewsDTO> companyNewsDTO = fetchCompanyNews(symbol);
        boolean newForToday = checkCompanyNewsToday(symbol);
        if(companyNewsDTO == null || companyNewsDTO.isEmpty() || !newForToday){
            createCompanyNews(symbol);
            companyNewsDTO = fetchCompanyNews(symbol);
        }
        return companyNewsDTO;
    }

    public List<CompanyNewsDTO> fetchCompanyNews(String symbol){
        List<? extends CompanyNews> companyNews = jdbcTemplate.query("select * from company_news where related = ?",
                new Object[]{symbol}, new BeanPropertyRowMapper<>(CompanyNews.class));

        return finnhubMapper.mapNews(companyNews);
    }

    public boolean checkCompanyNewsToday(String symbol){
        List<? extends CompanyNews> companyNews = jdbcTemplate.query("select * from company_news where related = ? and datetime = CURDATE()",
                new Object[]{symbol}, new BeanPropertyRowMapper<>(CompanyNews.class));

        List<CompanyNewsDTO> mapped = finnhubMapper.mapNews(companyNews);

        return mapped != null && !mapped.isEmpty();
    }

    @Transactional
    public boolean createCompanyNews(String symbol){
        List<CompanyNewsDTO> companyNewsDTO = this.getCompanyNews(symbol);

        String sql = "INSERT INTO company_news values(?, ?, ?, ?, ?, ?, ?, ?, ?, null)";
        int[] rows = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CompanyNewsDTO news = companyNewsDTO.get(i);
                ps.setString(1, news.getCategory());
                ps.setString(2, news.getRelated());
                ps.setDate(3, news.getDatetime());
                ps.setString(4, news.getHeadline());
                ps.setString(5, news.getImage());
                ps.setString(6, news.getSource());
                ps.setString(7, news.getSummary());
                ps.setString(8, news.getUrl());
                ps.setInt(9, news.getId());
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

    public List<StockDTO> getAllStocksFromDB() {
        List<StockDTO> stockDTO = fetchAllStocks();
        if(stockDTO == null || stockDTO.isEmpty()){
            createStock();
            stockDTO = fetchAllStocks();
        }
        return stockDTO.isEmpty() ? null : stockDTO;
    }

    public List<StockDTO> getAllStocksFromDB(String companyName, int limit) {
        List<StockDTO> stockDTO = fetchAllStocks(companyName, limit);
        if(stockDTO.isEmpty()){
            createStock();
            stockDTO = fetchAllStocks(companyName, limit);
        }
        return stockDTO.isEmpty() ? null : stockDTO;
    }

    @Transactional
    public List<StockDTO> fetchAllStocks() {
        List<Stock> stocks = jdbcTemplate.query("select * from stock",
                new Object[]{}, new BeanPropertyRowMapper<>(Stock.class));

        return finnhubMapper.mapStocksFromStock(stocks);
    }

    @Transactional
    public List<StockDTO> fetchAllStocks(String companyName, int limit) {
        List<Stock> stocks = jdbcTemplate.query("select * from stock where lower(description) like lower(?) limit ?",
                new Object[]{"%" + companyName + "%", limit}, new BeanPropertyRowMapper<>(Stock.class));
        return finnhubMapper.mapStocksFromStock(stocks);
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

    public boolean createPortfolio(SharesDTO sharesDTO){
        QuoteDTO quote = getQuoteFromDB(sharesDTO.getSymbol(), LocalDate.now());
        StockDTO stockDTO = getStockBySymbolFromDB(sharesDTO.getSymbol());

        double currentPrice = quote.getCurrentPrice();
        double shares = sharesDTO.getShares();

        String sql = "INSERT INTO portfolio values(null, ?, ?, ?, CURDATE(), ?, ?)";
        int rows = jdbcTemplate.update(sql, sharesDTO.getSymbol(), currentPrice,
                shares, stockDTO.getCurrency(), Transaction.BUY.toString()
        );
        return rows == 1;
    }

    public List<SharesDTO> getAllSharesFromPortfolio() {
        String sql = "select symbol, sum(case when type = 'BUY' then shares when type = 'SELL' then -shares else 0 end) as shares from portfolio group by symbol having shares > 0";
        List<SharesDTO> shares = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(SharesDTO.class));
        return shares.isEmpty() ? null : shares;
    }

    public List<SharesDTO> getAllSharesFromPortfolioPrices() {
        String sql = "select symbol, sum(case when type = 'BUY' then shares when type = 'SELL' then -shares else 0 end) as shares from portfolio group by symbol having shares > 0";
        List<SharesDTO> shares = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(SharesDTO.class));
        if (shares.isEmpty()) {
            return null;
        }

        for (SharesDTO dto : shares) {
            double currentPrice = getQuoteFromDB(dto.getSymbol(), LocalDate.now()).getCurrentPrice();
            double result = dto.getShares() * currentPrice;
            dto.setShares(result);
        }


        return shares;
    }

    public List<AssetTableDTO> getAllFromPortfolio() {
        List<PortfolioDTO> portfolioDTO = fetchPortfolio();

        if(portfolioDTO == null){
            return null;
        }

        return calculateFinancials(portfolioDTO);
    }

    public NetPaidDTO getNetAndPaid(){
        List<AssetTableDTO> assetTableDTO = getAllFromPortfolio();
        if(assetTableDTO == null){
            return null;
        }

        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalNetWorth = BigDecimal.ZERO;

        for (AssetTableDTO dto : assetTableDTO) {
            totalPaid = totalPaid.add(BigDecimal.valueOf(dto.getPaidAmount()));
            totalNetWorth = totalNetWorth.add(BigDecimal.valueOf(dto.getNetWorth()));
        }

        totalPaid = totalPaid.setScale(2, RoundingMode.HALF_UP);
        totalNetWorth = totalNetWorth.setScale(2, RoundingMode.HALF_UP);

        return new NetPaidDTO(totalNetWorth, totalPaid);
    }

    private List<PortfolioDTO> fetchPortfolio(){
        String sql = "select * from portfolio order by purchase_date";
        List<PortfolioDTO> portfolioDTO = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(PortfolioDTO.class));
        return portfolioDTO.isEmpty() ? null : portfolioDTO;
    }

    private List<AssetTableDTO> calculateFinancials(List<PortfolioDTO> portfolioDTO) {
        Map<String, AssetTableDTO> assetMap = new HashMap<>();

        for (PortfolioDTO dto : portfolioDTO) {
            String symbol = dto.getSymbol();
            String companyName = getStockBySymbolFromDB(symbol).getDescription();
            Transaction type = dto.getType();
            BigDecimal shares = BigDecimal.valueOf(dto.getShares());
            BigDecimal price = BigDecimal.valueOf(dto.getPricePerShare());

            AssetTableDTO assetDTO = assetMap.computeIfAbsent(symbol, k -> {
                AssetTableDTO newAsset = new AssetTableDTO();
                newAsset.setCompany(companyName);
                newAsset.setSymbol(symbol);
                newAsset.setShares(0);
                newAsset.setPaidAmount(0);
                newAsset.setProfit(0);
                newAsset.setNetWorth(0);
                return newAsset;
            });

            if (type == Transaction.BUY) {
                assetDTO.setShares(assetDTO.getShares() + shares.doubleValue());

                BigDecimal paid = BigDecimal.valueOf(assetDTO.getPaidAmount())
                        .add(shares.multiply(price))
                        .setScale(2, RoundingMode.HALF_UP);
                assetDTO.setPaidAmount(paid.doubleValue());

            } else if (type == Transaction.SELL) {
                BigDecimal saleProceeds = shares.multiply(price).setScale(2, RoundingMode.HALF_UP);

                BigDecimal avgCostPerShare = assetDTO.getShares() > 0
                        ? BigDecimal.valueOf(assetDTO.getPaidAmount())
                        .divide(BigDecimal.valueOf(assetDTO.getShares()), 2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

                assetDTO.setShares(assetDTO.getShares() - shares.doubleValue());
                if (avgCostPerShare.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                BigDecimal costOfSoldShares = avgCostPerShare.multiply(shares).setScale(2, RoundingMode.HALF_UP);
                BigDecimal realizedProfit = saleProceeds.subtract(costOfSoldShares).setScale(2, RoundingMode.HALF_UP);

                assetDTO.setProfit(
                        BigDecimal.valueOf(assetDTO.getProfit())
                                .add(realizedProfit)
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue()
                );

                assetDTO.setPaidAmount(
                        BigDecimal.valueOf(assetDTO.getPaidAmount())
                                .subtract(costOfSoldShares)
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue()
                );
            }
        }

        for (AssetTableDTO asset : assetMap.values()) {
            BigDecimal currentPrice = BigDecimal.valueOf(getQuoteFromDB(asset.getSymbol(), LocalDate.now()).getCurrentPrice());
            BigDecimal totalWorth = BigDecimal.valueOf(asset.getShares())
                    .multiply(currentPrice)
                    .setScale(2, RoundingMode.HALF_UP);

            asset.setCurrentPrice(currentPrice.setScale(2, RoundingMode.HALF_UP).doubleValue());
            asset.setTotalWorth(totalWorth.doubleValue());
            asset.setNetWorth(
                    totalWorth.add(BigDecimal.valueOf(asset.getProfit()))
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue()
            );
        }

        return assetMap.values().stream()
                .filter(asset -> asset.getShares() != 0)
                .collect(Collectors.toList());
    }

    public List<String> getAllSymbolsFromPortfolio() {
        String sql = "select distinct symbol from portfolio";
        List<String> symbols = jdbcTemplate.queryForList(sql, String.class);
        return symbols.isEmpty() ? null : symbols;
    }

    public boolean updateQuotesForExistingStocks() {
        List<String> symbols = getAllSymbolsFromPortfolio();
        for(String symbol : symbols){
            createQuote(symbol);
        }
        return true;
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
