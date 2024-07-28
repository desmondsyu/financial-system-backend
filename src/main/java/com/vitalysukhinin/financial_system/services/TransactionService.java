package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.dto.CustomPage;
import com.vitalysukhinin.financial_system.dto.TransactionGroupResponse;
import com.vitalysukhinin.financial_system.dto.TransactionResponse;
import com.vitalysukhinin.financial_system.dto.UserSimple;
import com.vitalysukhinin.financial_system.entities.Label;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.entities.TransactionGroup;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.LabelRepository;
import com.vitalysukhinin.financial_system.repositories.TransactionGroupRepository;
import com.vitalysukhinin.financial_system.repositories.TransactionRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final TransactionGroupRepository transactionGroupRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, LabelRepository labelRepository, TransactionGroupRepository transactionGroupRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
        this.transactionGroupRepository = transactionGroupRepository;
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

    public CustomPage<TransactionResponse> getTransactions(String name, LocalDateTime from, LocalDateTime to, String labelName, Integer type, String groupName, Pageable pageable) {
        User user = userRepository.findByUsername(name).orElse(null);
        TransactionGroup group = transactionGroupRepository.findByNameAndUser(groupName, user).orElse(null);
        Label label = labelRepository.findByName(labelName).orElse(null);

        Specification<Transaction> specification = TransactionSearchFilter.filters(user, from, to, label, type, group);
        Page<Transaction> transactions = transactionRepository.findAll(specification, pageable);
        List<TransactionResponse> content = transactions.map(transaction -> new TransactionResponse(
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
        )).stream().toList();
        CustomPage<TransactionResponse> response = new CustomPage<>();
        response.setContent(content);
        response.setPageNumber(transactions.getNumber());
        response.setPageSize(transactions.getSize());
        response.setTotalElements(transactions.getTotalElements());
        response.setTotalPages(transactions.getTotalPages());
        return response;
    }
}