package com.restapi.restapi.controller;

import com.restapi.restapi.dto.QuoteDTO;
import com.restapi.restapi.model.Quote;
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

    @GetMapping("/quote_example/{symbol}")
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
                ResponseEntity.badRequest().body("Inserted failed!");
    }

    @PutMapping("/quotes/{id}")
    public ResponseEntity<String> putQuote(@PathVariable String symbol, @RequestBody QuoteDTO quoteDTO){
        boolean success = finService.putQuote(quoteDTO, symbol);

        return success ? ResponseEntity.ok("Successfully done the update!") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No quotes with this symbol!");
    }

    @DeleteMapping("/quotes/{id}")
    public ResponseEntity<String> deleteQuote(@PathVariable String symbol){
        boolean success = finService.deleteQuote(symbol);
        return success ? ResponseEntity.ok("Successfully deleted") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No quote with this id!");
    }
}
