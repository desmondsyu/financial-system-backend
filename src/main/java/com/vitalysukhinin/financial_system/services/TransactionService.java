package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.dto.*;
import com.vitalysukhinin.financial_system.entities.*;
import com.vitalysukhinin.financial_system.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final TransactionGroupRepository transactionGroupRepository;
    private final PdfGenerationService pdfGenerationService;
    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, LabelRepository labelRepository, TransactionGroupRepository transactionGroupRepository, PdfGenerationService pdfGenerationService, TransactionTypeRepository transactionTypeRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
        this.transactionGroupRepository = transactionGroupRepository;
        this.pdfGenerationService = pdfGenerationService;
        this.transactionTypeRepository = transactionTypeRepository;
    }

    public Optional<Transaction> addTransaction(Transaction transaction) {
        Optional<Transaction> result = Optional.empty();
        Optional<User> optionalUser = userRepository.findByEmail(transaction.getUser().getEmail());
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
        Optional<User> optionalUser = userRepository.findByEmail(transaction.getUser().getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            transaction.setUser(user);
            transaction.getTransactionGroup().setUser(user);
            Transaction savedTransaction = transactionRepository.save(transaction);
            result = Optional.of(savedTransaction);
        }
        return result;
    }

    public CustomPage<TransactionResponse> getTransactions(String email, LocalDateTime from, LocalDateTime to, String labelName, Integer typeId, String groupName, Boolean all, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElse(null);
        TransactionGroup group = transactionGroupRepository.findByNameAndUser(groupName, user).orElse(null);
        Label label = labelRepository.findByName(labelName).orElse(null);
        TransactionType type = transactionTypeRepository.findById(typeId == null ? -1 : typeId).orElse(null);
        Specification<Transaction> specification = TransactionSearchFilter.filters(user, from, to, label, type, group);
        CustomPage<TransactionResponse> response = new CustomPage<>();
        List<TransactionResponse> content;
        boolean getAll = all == null ? false : all.booleanValue();
        if (getAll) {
            List<Transaction> transactions = transactionRepository.findAll(specification);
            content = transactions.stream().map(convert()).toList();
            response.setTotalElements(transactions.size());
        } else {
            Page<Transaction> transactions = transactionRepository.findAll(specification, pageable);
            content = transactions.map(convert()).stream().toList();
            response.setPageNumber(transactions.getNumber());
            response.setPageSize(transactions.getSize());
            response.setTotalElements(transactions.getTotalElements());
            response.setTotalPages(transactions.getTotalPages());
        }
        response.setContent(content);
        return response;
    }

    private Function<Transaction, TransactionResponse> convert() {
        return transaction -> new TransactionResponse(
                transaction.getId(),
                new UserSimple(transaction.getUser().getEmail()),
                transaction.getHashcode(),
                transaction.getTransactionGroup() == null ? null :
                        new TransactionGroupResponse(
                        transaction.getTransactionGroup().getId(),
                        transaction.getTransactionGroup().getName(),
                        transaction.getTransactionGroup().getTransactionType(),
                        transaction.getTransactionGroup().getUser() == null ? null :
                            new UserSimple(transaction.getTransactionGroup().getUser().getEmail())
                ),
                transaction.getLabel() == null ? null :
                        new LabelResponse(transaction.getLabel().getId(),
                        transaction.getLabel().getName(),
                        transaction.getLabel().getUser() == null ? null :
                            new UserSimple(transaction.getLabel().getUser().getEmail())),
                transaction.getTransactionDate(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getBalance()
        );
    }

    public List<Transaction> getTransactions(User user, LocalDateTime from, LocalDateTime to, String labelName, Integer typeId, String groupName) {
        TransactionGroup group = transactionGroupRepository.findByNameAndUser(groupName, user).orElse(null);
        Label label = labelRepository.findByName(labelName).orElse(null);
        TransactionType type = transactionTypeRepository.findById(typeId).orElse(null);
        Specification<Transaction> specification = TransactionSearchFilter.filters(user, from, to, label, type, group);
        List<Transaction> transactions = transactionRepository.findAll(specification);
        return transactions;
    }

    public byte[] generateUserTransactionPdf(String email, LocalDateTime from, LocalDateTime to, String label, Integer type, String group) {
        User user = userRepository.findByEmail(email).orElse(null);
        List<Transaction> transactions = getTransactions(user, from, to, label, type, group);

        return pdfGenerationService.generateUserTransactionPdf(user, transactions);
    }
}