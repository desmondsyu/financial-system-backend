package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.components.ParserFactory;
import com.vitalysukhinin.financial_system.components.TransactionParseResult;
import com.vitalysukhinin.financial_system.dto.*;
import com.vitalysukhinin.financial_system.entities.*;
import com.vitalysukhinin.financial_system.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final PdfGenerationService pdfGenerationService;
    private final TransactionTypeRepository transactionTypeRepository;
    private final TransactionGroupRepository transactionGroupRepository;
    private final ParserFactory parserFactory;
    private final TransactionConverterService transactionConverterService;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, LabelRepository labelRepository, PdfGenerationService pdfGenerationService, TransactionTypeRepository transactionTypeRepository, TransactionGroupRepository transactionGroupRepository, ParserFactory parserFactory, TransactionConverterService transactionConverterService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
        this.pdfGenerationService = pdfGenerationService;
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionGroupRepository = transactionGroupRepository;
        this.parserFactory = parserFactory;
        this.transactionConverterService = transactionConverterService;
    }

    public Optional<Transaction> addTransaction(Transaction transaction) {
        Optional<Transaction> result = Optional.empty();
        Optional<User> optionalUser = userRepository.findByEmail(transaction.getUser().getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            transaction.setUser(user);
            if (transaction.getTransactionGroup() != null) {
                User transactionGroupUser;
                if (transaction.getTransactionGroup().getUser() == null)
                    transactionGroupUser = null;
                else
                    transactionGroupUser = user;
                transaction.getTransactionGroup().setUser(transactionGroupUser);
            }
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setHashcode(
                    String.valueOf(Objects.hash(transaction.getUser(),
                            transaction.getTransactionDate(),
                            transaction.getCreatedAt(),
                            transaction.getAmount(),
                            transaction.getDescription(),
                            transaction.getLabel()))
            );
            if (transaction.getLabel() != null) {
                User transactionLabelUser;
                if (transaction.getLabel().getUser() == null)
                    transactionLabelUser = null;
                else
                    transactionLabelUser = user;
                transaction.getLabel().setUser(transactionLabelUser);
            }
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
        TransactionGroup group = transactionGroupRepository.findByNameAndUserOrNoUser(groupName, user).orElse(null);
        Label label = labelRepository.findByName(labelName).orElse(null);
        TransactionType type = transactionTypeRepository.findById(typeId == null ? -1 : typeId).orElse(null);
        Specification<Transaction> specification = TransactionSearchFilter.filters(user, from, to, label, type, group);
        CustomPage<TransactionResponse> response = new CustomPage<>();
        List<TransactionResponse> content;
        boolean getAll = all == null ? false : all.booleanValue();
        if (getAll) {
            List<Transaction> transactions = transactionRepository.findAll(specification);
            content = transactions.stream().map(transactionConverterService.convertTransactionToResponse()).toList();
            response.setTotalElements(transactions.size());
        } else {
            Page<Transaction> transactions = transactionRepository.findAll(specification, pageable);
            content = transactions.map(transactionConverterService.convertTransactionToResponse()).stream().toList();
            response.setPageNumber(transactions.getNumber());
            response.setPageSize(transactions.getSize());
            response.setTotalElements(transactions.getTotalElements());
            response.setTotalPages(transactions.getTotalPages());
        }
        response.setContent(content);
        return response;
    }

    public List<Transaction> getTransactions(User user, LocalDateTime from, LocalDateTime to, String labelName, Integer typeId, String groupName) {
        TransactionGroup group = transactionGroupRepository.findByNameAndUserOrNoUser(groupName, user).orElse(null);
        Label label = labelRepository.findByName(labelName).orElse(null);
        TransactionType type = typeId != null ? transactionTypeRepository.findById(typeId).orElse(null) : null;
        Specification<Transaction> specification = TransactionSearchFilter.filters(user, from, to, label, type, group);
        List<Transaction> transactions = transactionRepository.findAll(specification);
        return transactions;
    }

    public byte[] generateUserTransactionPdf(String email, LocalDateTime from, LocalDateTime to, String label, Integer type, String group) {
        User user = userRepository.findByEmail(email).orElse(null);
        List<Transaction> transactions = getTransactions(user, from, to, label, type, group);

        return pdfGenerationService.generateUserTransactionPdf(user, transactions);
    }

    public TransactionParseResultResponse parseTransactions(MultipartFile file, String email) throws IOException {
        String fileName = file.getOriginalFilename();
        TransactionParser parser = parserFactory.getTransactionParser(fileName.substring(fileName.lastIndexOf(".") + 1));
        TransactionParseResult result = parser.parse(file.getInputStream(), email);
        transactionRepository.saveAll(result.successfulTransactions());
        return new TransactionParseResultResponse(
                result.successfulTransactions()
                        .stream()
                        .map(transactionConverterService.convertTransactionToResponse())
                        .toList(),
                result.failedTransactions()
        );
    }
}