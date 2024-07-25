package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.dto.TransactionGroupResponse;
import com.vitalysukhinin.financial_system.dto.TransactionResponse;
import com.vitalysukhinin.financial_system.dto.UserSimple;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TransactionRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionResponse> response = transactions.stream().map(transaction ->
                new TransactionResponse(
                        new UserSimple(transaction.getUser().getUsername()),
                        transaction.getHashcode(),
                        new TransactionGroupResponse(
                                transaction.getTransactionGroup().getId(),
                                transaction.getTransactionGroup().getName(),
                                transaction.getTransactionGroup().getTransactionType(),
                                new UserSimple(transaction.getUser().getUsername())
                        ),
                        transaction.getLabel(),
                        transaction.getTransactionDate(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getBalance()
                )).toList();
        return response;
    }

    public Optional<Transaction> addTransaction(Transaction transaction) {
        Optional<Transaction> result = Optional.empty();
        Optional<User> optionalUser = userRepository.findByUsername(transaction.getUser().getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            transaction.setUser(user);
            transaction.getTransactionGroup().setUser(user);
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setHashcode(
                    String.valueOf(Objects.hash(transaction.getUser(),
                            transaction.getTransactionDate(),
                            transaction.getCreatedAt(),
                            transaction.getAmount(),
                            transaction.getDescription(),
                            transaction.getLabel()))
            );
            Transaction savedTransaction = transactionRepository.save(transaction);
            result = Optional.of(savedTransaction);
        }
        return result;
    }

    public void deleteTransaction(Integer id) {
        transactionRepository.deleteById(id);
    }

    public Optional<Transaction> updateTransaction(Transaction transaction) {
        Optional<Transaction> result = Optional.empty();
        Optional<User> optionalUser = userRepository.findByUsername(transaction.getUser().getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            transaction.setUser(user);
            transaction.getTransactionGroup().setUser(user);
            Transaction savedTransaction = transactionRepository.save(transaction);
            result = Optional.of(savedTransaction);
        }
        return result;
    }
//    public List<Transaction> getTransactionsWithCriteria(String name, Date from, Date to, String label, Integer type, String group)
//    {
//        TransactionRepository transactionRepository = this.transactionRepository;
//        return transactionRepository.findAll(TransactionSearchFilter.filters(name, from, to, label, type, group));
//
//    }
}