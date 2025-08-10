package com.restapi.restapi.controller;

import com.restapi.restapi.dto.*;
import com.restapi.restapi.service.FinanceDataService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
public class FinanceDataController {

    private final FinanceDataService finService;

    public FinanceDataController(final FinanceDataService finService){
        this.finService = finService;
    }

    // FROM DB
    @GetMapping("/quote/{symbol}")
    public ResponseEntity<QuoteDTO> getQuote(@PathVariable String symbol,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        QuoteDTO quoteDTO = finService.getQuoteFromDB(symbol, date);
        return quoteDTO != null ? ResponseEntity.ok(quoteDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("/quotes")
    public ResponseEntity<List<QuoteDTO>> getQuotes(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        List<QuoteDTO> quotes = finService.getAllQuotes(date);

        return quotes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(quotes);
    }

    // TO DB
    @PostMapping("/quote")
    public ResponseEntity<String> postQuote(@RequestParam String symbol){
        boolean success = finService.createQuote(symbol);

        return success ? ResponseEntity.ok("Successfully inserted " + symbol + " symbol!") :
                ResponseEntity.badRequest().body("Inserted failed, quote " + symbol + " already exists for current day!");
    }

    //Company News

    // FROM DB
    @GetMapping("/company_news/{symbol}")
    public ResponseEntity<List<CompanyNewsDTO>> getCompanyNews(@PathVariable String symbol){
        List<CompanyNewsDTO> companyNewsDTO = finService.getCompanyNewsFromDB(symbol);
        return (companyNewsDTO == null || companyNewsDTO.isEmpty()) ?
                ResponseEntity.ok(Collections.singletonList(new CompanyNewsDTO())) :
                ResponseEntity.ok(companyNewsDTO);
    }

    // TO DB
    @PostMapping("/company_news")
    public ResponseEntity<String> postCompanyNews(@RequestParam String symbol){
        boolean success = finService.createCompanyNews(symbol);

        return success ? ResponseEntity.ok("Successfully inserted " + symbol + " symbol!") :
                ResponseEntity.badRequest().body("Inserted failed, all news exist already!");
    }

    //Stock

    // TO DB
    @PostMapping("/stock")
    public ResponseEntity<String> postStock(){
        boolean success = finService.createStock();

        return success ? ResponseEntity.ok("Successfully inserted stock!") :
                ResponseEntity.badRequest().body("Inserted failed!");
    }

    // FROM DB
    @GetMapping("/stock")
    public ResponseEntity<List<StockDTO>> getStock(){
        List<StockDTO> stockDTO = finService.getAllStocksFromDB();

        return stockDTO.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(stockDTO);
    }

    @GetMapping("/stock/companyName")
    public ResponseEntity<List<StockDTO>> getStockBySymbol(@RequestParam String companyName, @RequestParam int limit){
        List<StockDTO> stockDTO = finService.getAllStocksFromDB(companyName, limit);

        return stockDTO.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(stockDTO);
    }

    // TO DB
    @PostMapping("/portfolio")
    public ResponseEntity<String> postPortfolio(@RequestBody SharesDTO sharesDTO){
        boolean success = finService.createPortfolio(sharesDTO);

        return success ? ResponseEntity.ok("Successfully inserted stock into the portfolio!") :
                ResponseEntity.badRequest().body("Inserted failed!");
    }

    // FROM DB
    @GetMapping("/portfolio")
    public ResponseEntity<List<SharesDTO>> getPortfolioShares(){
        List<SharesDTO> sharesDTO = finService.getAllSharesFromPortfolio();

        return sharesDTO.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(sharesDTO);
    }

    // FROM DB
    @GetMapping("/portfolio/byPrices")
    public ResponseEntity<List<SharesDTO>> getPortfolioSharesPrices(){
        List<SharesDTO> sharesDTO = finService.getAllSharesFromPortfolioPrices();

        return sharesDTO.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(sharesDTO);
    }

    // FROM DB
    @GetMapping("/portfolio/holdings")
    public ResponseEntity<List<AssetTableDTO>> getPortfolioHoldings(){
        List<AssetTableDTO> assetTableDTO = finService.getAllFromPortfolio();

        return assetTableDTO.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(assetTableDTO);
    }

    // FROM DB
    @GetMapping("/portfolio/netPaid")
    public ResponseEntity<NetPaidDTO> getNetWorthAndPaid(){
        NetPaidDTO netPaidDTO = finService.getNetAndPaid();

        return netPaidDTO == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(netPaidDTO);
    }

    // FROM DB
    @DeleteMapping("/portfolio")
    public ResponseEntity<String> deletePortfolioAllSymbols(@RequestBody SharesDTO sharesDTO){
        boolean success = finService.sellFromPortfolio(sharesDTO);

        return success ? ResponseEntity.ok("Successfully sold!") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock either not in the portfolio, or you tried to sell more than you have!");
    }

    // FROM DB
    @PutMapping("/quote")
    public ResponseEntity<String> getSymbolsFromPortfolio(@RequestBody QuoteDTO quoteDTO){
        boolean success = finService.putQuote(quoteDTO);

        return success ? ResponseEntity.ok("Success!") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Couldn't update one or more stocks current price!");
    }
}
