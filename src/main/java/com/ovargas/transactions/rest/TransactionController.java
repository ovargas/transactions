package com.ovargas.transactions.rest;

import com.ovargas.transactions.rest.dto.Statistics;
import com.ovargas.transactions.rest.dto.TransactionInfo;
import com.ovargas.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TransactionController.BASE_PATH)
public class TransactionController {

    static final String BASE_PATH = "/";

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST, value = "transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addTransaction(@RequestBody TransactionInfo transactionInfo) {

        if (!transactionService.addTransaction(transactionInfo)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "statistics")
    @ResponseStatus(HttpStatus.OK)
    public Statistics getStatistics() {
        return transactionService.calculate();
    }
}