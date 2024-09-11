package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.entities.RecurringTransaction;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.RecurringTransactionRepository;
import com.vitalysukhinin.financial_system.repositories.TransactionRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecurringTransactionService {
    private final RecurringTransactionRepository recurringTransactionRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public RecurringTransactionService(RecurringTransactionRepository recurringTransactionRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public void deleteById(Integer id) {
        recurringTransactionRepository.deleteById(id);
    }

    public void update(RecurringTransaction transaction) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transaction.getTransaction().getId());
        if (transactionOptional.isPresent()) {
            transaction.setTransaction(transactionOptional.get());
            recurringTransactionRepository.save(transaction);
        }
    }

    public Optional<RecurringTransaction> create(RecurringTransaction transaction) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transaction.getTransaction().getId());
        if (transactionOptional.isPresent()) {
            transaction.setTransaction(transactionOptional.get());
            RecurringTransaction saved = recurringTransactionRepository.save(transaction);
            return Optional.of(saved);
        }
        return Optional.empty();
    }

    public List<RecurringTransaction> getAll(Authentication auth) {
        Optional<User> optionalUser = userRepository.findByEmail(auth.getName());
        if (optionalUser.isPresent())
            return recurringTransactionRepository.findAllByUser(optionalUser.get());
        return List.of();
    }

    public List<RecurringTransaction> getAll() {
        return recurringTransactionRepository.findAll();
    }

    public Optional<RecurringTransaction> getByTransactionId(Integer transactionId) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        if (optionalTransaction.isPresent())
            return recurringTransactionRepository.findByTransaction(optionalTransaction.get());
        else
            return Optional.empty();
    }

    public Optional<RecurringTransaction> getRecurringTransaction(Integer recurringTransactionId) {
        return recurringTransactionRepository.findById(recurringTransactionId);
    }
}
