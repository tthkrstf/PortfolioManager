package com.restapi.restapi.controller;

import com.restapi.restapi.dto.PasswordsDTO;
import com.restapi.restapi.dto.QuoteDTO;
import com.restapi.restapi.model.Quote;
import com.restapi.restapi.service.FinanceDataService;
import com.restapi.restapi.service.PasswordsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/quote")
    public ResponseEntity<String> postPasswords(@RequestParam String symbol){
        boolean success = finService.createQuote(symbol);

        return success ? ResponseEntity.ok("Successfully inserted " + symbol + " symbol!") :
                ResponseEntity.badRequest().body("Inserted failed!");
    }
}
