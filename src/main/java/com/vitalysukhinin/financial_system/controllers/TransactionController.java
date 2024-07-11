package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.entities.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    List<Transaction> transactions = new ArrayList<>();
    {
        transactions.addAll(List.of(
                new Transaction("transaction1"),
                new Transaction("transaction2"),
                new Transaction("transaction3"),
                new Transaction("transaction4")
        ));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions() {
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        transactions.add(transaction);
        return ResponseEntity.ok(transaction);
    }


}
