package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.dto.RecurringTransactionResponse;
import com.vitalysukhinin.financial_system.entities.RecurringTransaction;
import com.vitalysukhinin.financial_system.services.ConverterService;
import com.vitalysukhinin.financial_system.services.RecurringTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recurring-transactions")
public class RecurringTransactionController {


    private final RecurringTransactionService recurringTransactionService;
    private final ConverterService converterService;

    public RecurringTransactionController(RecurringTransactionService recurringTransactionService, ConverterService converterService) {
        this.recurringTransactionService = recurringTransactionService;
        this.converterService = converterService;
    }

    @GetMapping("/by-transaction/{transactionId}")
    public ResponseEntity<RecurringTransactionResponse> getByTransactionId(@PathVariable Integer transactionId) {
        Optional<RecurringTransaction> transaction = recurringTransactionService.getByTransactionId(transactionId);

        if (transaction.isPresent())
            return ResponseEntity.ok(converterService.convertRecurringTransactionToResponse().apply(transaction.get()));
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/{recurringTransactionId}")
    public ResponseEntity<RecurringTransactionResponse> getRecurringTransaction(@PathVariable Integer recurringTransactionId) {
        Optional<RecurringTransaction> transaction = recurringTransactionService.getRecurringTransaction(recurringTransactionId);

        if (transaction.isPresent())
            return ResponseEntity.ok(converterService.convertRecurringTransactionToResponse().apply(transaction.get()));
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<RecurringTransactionResponse>> getAll(Authentication auth) {
        return ResponseEntity.ok(recurringTransactionService.getAll(auth).stream().map(converterService.convertRecurringTransactionToResponse()).toList());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody RecurringTransaction transaction) {
        Optional<RecurringTransaction> recurringTransaction = recurringTransactionService.create(transaction);
        if (recurringTransaction.isPresent())
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("Some error occurred during creation of recurring transaction");
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody RecurringTransaction transaction) {
        recurringTransactionService.update(transaction);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        recurringTransactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
