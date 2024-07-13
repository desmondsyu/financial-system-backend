package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.entities.TransactionGroup;
import com.vitalysukhinin.financial_system.entities.TransactionType;
import com.vitalysukhinin.financial_system.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    List<Transaction> transactions = new ArrayList<>();
    {
        User user = new User(1, "Vitaly", "pass", LocalDateTime.now(), "v@v.com", "single", true);
        TransactionType income = new TransactionType(1, "Income");
        TransactionType expense = new TransactionType(1, "Expense");
        TransactionGroup transactionGroup = new TransactionGroup(1, "Salary", income, user);
        TransactionGroup transactionGroupExp = new TransactionGroup(2, "Groceries", expense, user);
        transactions.addAll(List.of(
                new Transaction(1, user, "abc", transactionGroup, null, LocalDateTime.now(), 100.0, "", 100.0),
                new Transaction(2, user, "bcd", transactionGroupExp, null, LocalDateTime.now(), 50.0, "", 50.0)
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
