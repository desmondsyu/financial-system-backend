package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.dto.CustomPage;
import com.vitalysukhinin.financial_system.dto.TransactionResponse;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.services.TransactionService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Optional<Transaction> result = transactionService.addTransaction(transaction);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping
    public ResponseEntity<CustomPage<TransactionResponse>> getTransactions(
            @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam (required = false) String label,
            @RequestParam (required = false) Integer type,
            @RequestParam (required = false) String group,
            Authentication authentication,
            Pageable pageable)
    {
        CustomPage<TransactionResponse> result = transactionService.getTransactions(
                authentication.getName(), from, to ,label, type, group, pageable
        );
        return ResponseEntity.ok(result);
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
