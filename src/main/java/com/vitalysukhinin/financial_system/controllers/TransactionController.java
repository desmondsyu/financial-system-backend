package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.dto.TransactionResponse;
import com.vitalysukhinin.financial_system.dto.UserSimple;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.dto.TransactionGroupResponse;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TransactionRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import com.vitalysukhinin.financial_system.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Optional<Transaction> result = transactionService.addTransaction(transaction);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping("/criteria-filter")
    public List<Transaction> getTransactionWithCriteria(
            @RequestParam (required = false) Date from,
            @RequestParam (required = false)Date to,
            @RequestParam (required = false)String label,
            @RequestParam (required = false)int type,
            @RequestParam (required = false)String group,
            Authentication authentication)
    {
        return transactionService.getTransactionsWithCriteria(
               authentication.getName(), from, to ,label, type, group
        );
    }

    @PutMapping
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction) {
        Optional<Transaction> result = transactionService.updateTransaction(transaction);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }


}
