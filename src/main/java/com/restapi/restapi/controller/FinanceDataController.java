package com.restapi.restapi.controller;

import com.restapi.restapi.dto.CompanyNewsDTO;
import com.restapi.restapi.dto.QuoteDTO;
import com.restapi.restapi.dto.StockDTO;
import com.restapi.restapi.model.CompanyNews;
import com.restapi.restapi.model.Quote;
import com.restapi.restapi.model.Stock;
import com.restapi.restapi.service.FinanceDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FinanceDataController {

    private final FinanceDataService finService;

    public FinanceDataController(final FinanceDataService finService){
        this.finService = finService;
    }

    @GetMapping("/quote/{symbol}")
    public ResponseEntity<QuoteDTO> getQuote(@PathVariable String symbol){
        QuoteDTO quoteDTO = finService.getQuote(symbol);
        return ResponseEntity.ok(quoteDTO);
    }

    @GetMapping("/quotes")
    public ResponseEntity<List<Quote>> getQuotes(){
        List<Quote> quotes = finService.getAllQuotes();

        return quotes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(quotes);
    }

    @PostMapping("/quote")
    public ResponseEntity<String> postQuote(@RequestParam String symbol){
        boolean success = finService.createQuote(symbol);

        return success ? ResponseEntity.ok("Successfully inserted " + symbol + " symbol!") :
                ResponseEntity.badRequest().body("Inserted failed, quote " + symbol + " already exists for current day!");
    }

    @PutMapping("/quotes/{symbol}")
    public ResponseEntity<String> putQuote(@PathVariable String symbol, @RequestBody QuoteDTO quoteDTO){
        boolean success = finService.putQuote(quoteDTO, symbol);

        return success ? ResponseEntity.ok("Successfully done the update!") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No quotes with this symbol!");
    }

    @DeleteMapping("/quotes/{symbol}")
    public ResponseEntity<String> deleteQuote(@PathVariable String symbol){
        boolean success = finService.deleteQuote(symbol);
        return success ? ResponseEntity.ok("Successfully deleted") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No quote with this symbol!");
    }


    //Company News

    @GetMapping("/company_news/{symbol}")
    public ResponseEntity<List<CompanyNewsDTO>> getCompanyNews(@PathVariable String symbol){
        List<CompanyNewsDTO> companyNewsDTO = finService.getCompanyNews(symbol);
        return ResponseEntity.ok(companyNewsDTO);
    }

    @GetMapping("/company_news")
    public ResponseEntity<List<CompanyNews>> getCompanyNews(){
        List<CompanyNews> news = finService.getAllCompanyNews();

        return news.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(news);
    }

    @PostMapping("/company_news")
    public ResponseEntity<String> postCompanyNews(@RequestParam String symbol){
        boolean success = finService.createCompanyNews(symbol);

        return success ? ResponseEntity.ok("Successfully inserted " + symbol + " symbol!") :
                ResponseEntity.badRequest().body("Inserted failed, all news exist already!");
    }

    //Stock
    @PostMapping("/stock")
    public ResponseEntity<String> postStock(){
        boolean success = finService.createStock();

        return success ? ResponseEntity.ok("Successfully inserted stock!") :
                ResponseEntity.badRequest().body("Inserted failed!");
    }

    @GetMapping("/stock/{symbol}")
    public ResponseEntity<List<StockDTO>> getStock(@PathVariable String symbol){
        List<StockDTO> stockDTOS = finService.getStockRaw();
        return ResponseEntity.ok(stockDTOS);
    }

    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>> getStocks(){
        List<Stock> stocks = finService.getAllStocks();

        return stocks.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(stocks);
    }
}
