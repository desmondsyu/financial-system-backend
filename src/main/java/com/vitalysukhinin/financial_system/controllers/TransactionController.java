package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.dto.TransactionResponse;
import com.vitalysukhinin.financial_system.dto.TransactionSearchFilter;
import com.vitalysukhinin.financial_system.dto.UserSimple;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.dto.TransactionGroupResponse;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TransactionRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import com.vitalysukhinin.financial_system.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
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
    public List<Transaction> getTransactionWithCriteria(@RequestBody TransactionSearchFilter transactionSearchFilter)
    {
        return transactionService.getTransactionsWithCriteria(transactionSearchFilter.getUser(), transactionSearchFilter.getFrom()
                , transactionSearchFilter.getTo(), transactionSearchFilter.getLabel(), transactionSearchFilter.getTransactionGroupResponse());
    }

}
