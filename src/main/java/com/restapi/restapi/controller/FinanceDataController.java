package com.restapi.restapi.controller;

import com.restapi.restapi.dto.CompanyNewsDTO;
import com.restapi.restapi.dto.PortfolioDTO;
import com.restapi.restapi.dto.QuoteDTO;
import com.restapi.restapi.dto.SharesDTO;
import com.restapi.restapi.model.Portfolio;
import com.restapi.restapi.model.Stock;
import com.restapi.restapi.service.FinanceDataService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
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
        return companyNewsDTO.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(companyNewsDTO);
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
    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>> getStocks(){
        List<Stock> stocks = finService.getAllStocks();

        return stocks.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(stocks);
    }

    // TO DB
    @PostMapping("/portfolio")
    public ResponseEntity<String> postPortfolio(@RequestBody PortfolioDTO portfolioDTO){
        boolean success = finService.createPortfolio(portfolioDTO);

        return success ? ResponseEntity.ok("Successfully inserted stock into the portfolio!") :
                ResponseEntity.badRequest().body("Inserted failed!");
    }

    // FROM DB
    @GetMapping("/portfolio")
    public ResponseEntity<List<SharesDTO>> getPortfolio(){
        List<SharesDTO> sharesDTO = finService.getAllFromPortfolio();

        return sharesDTO.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(sharesDTO);
    }

    // FROM DB
    @DeleteMapping("/portfolio")
    public ResponseEntity<String> deletePortfolioAllSymbols(@RequestBody SharesDTO sharesDTO){
        boolean success = finService.sellFromPortfolio(sharesDTO);

        return success ? ResponseEntity.ok("Successfully sold!") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock either not in the portfolio, or you tried to sell more than you have!");
    }
}
