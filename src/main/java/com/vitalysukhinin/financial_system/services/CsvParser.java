package com.vitalysukhinin.financial_system.services;

import com.opencsv.bean.CsvToBeanBuilder;
import com.vitalysukhinin.financial_system.components.FailedTransaction;
import com.vitalysukhinin.financial_system.components.TransactionParseResult;
import com.vitalysukhinin.financial_system.components.TransactionCsv;
import com.vitalysukhinin.financial_system.entities.*;
import com.vitalysukhinin.financial_system.repositories.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CsvParser implements TransactionParser{
    private final TransactionGroupRepository transactionGroupRepository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final Set<String> usedHashcodes = new HashSet<>();
    private final TransactionRepository transactionRepository;

    public CsvParser(TransactionGroupRepository transactionGroupRepository, TransactionTypeRepository transactionTypeRepository, UserRepository userRepository, LabelRepository labelRepository, TransactionRepository transactionRepository) {
        this.transactionGroupRepository = transactionGroupRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionParseResult parse(InputStream input, String email) throws IOException {
        try (Reader reader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(reader)) {

            List<TransactionCsv> transactionsCsvs = new CsvToBeanBuilder<TransactionCsv>(reader)
                    .withType(TransactionCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build()
                    .parse();

            List<Transaction> successfulTransactions = new ArrayList<>();
            List<FailedTransaction> failedTransactions = new ArrayList<>();
            Optional<User> userOptional = userRepository.findByEmail(email);
            User user;
            if (userOptional.isPresent())
                user = userOptional.get();
            else
                return new TransactionParseResult(successfulTransactions, failedTransactions);

            for (int i = 0; i < transactionsCsvs.size(); i++) {
                TransactionCsv transactionCsv = transactionsCsvs.get(i);

                if (transactionCsv.getType() == null) {
                    failedTransactions.add(new FailedTransaction(i + 1, "Transaction type is missing"));
                    continue;
                }
                int typeId;
                Optional<Integer> optionalType = getType(transactionCsv.getType());
                if (optionalType.isPresent()) {
                    typeId = optionalType.get();
                } else {
                    failedTransactions.add(new FailedTransaction(i + 1, "Wrong transaction type"));
                    continue;
                }

                if (transactionCsv.getAmount() == null) {
                    failedTransactions.add(new FailedTransaction(i + 1, "Transaction amount is missing"));
                    continue;
                }

                if (transactionCsv.getAmount() <= 0) {
                    failedTransactions.add(new FailedTransaction(i + 1, "Amount should be greater than zero"));
                    continue;
                }

                if (transactionCsv.getTransactionDate() == null) {
                    failedTransactions.add(new FailedTransaction(i + 1, "Transaction date is missing"));
                    continue;
                }

                Optional<TransactionType> typeOptional = transactionTypeRepository.findById(typeId);
                TransactionType type;
                if (typeOptional.isPresent())
                    type = typeOptional.get();
                else {
                    failedTransactions.add(new FailedTransaction(i + 1, "Transaction type not found"));
                    continue;
                }

                if (transactionCsv.getTransactionGroup() == null || transactionCsv.getTransactionGroup().isEmpty()) {
                    failedTransactions.add(new FailedTransaction(i + 1, "Transaction group is missing"));
                    continue;
                }

                Optional<TransactionGroup> groupOptional = transactionGroupRepository.findByNameAndUserOrNoUser(transactionCsv.getTransactionGroup(), user);
                TransactionGroup group;
                if (groupOptional.isPresent())
                    group = groupOptional.get();
                else {
                    group = new TransactionGroup();
                    group.setName(transactionCsv.getTransactionGroup());
                    group.setTransactionType(type);
                    group.setUser(user);
                    transactionGroupRepository.save(group);
                }

                Optional<Label> labelOptional = labelRepository.findByNameAndUserOrNoUser(transactionCsv.getLabel(), user);
                Label label;
                if (labelOptional.isPresent())
                    label = labelOptional.get();
                else {
                    label = new Label();
                    label.setName(transactionCsv.getLabel());
                    label.setUser(user);
                    labelRepository.save(label);
                }
                LocalDateTime createdAt = LocalDateTime.now();
                String hashCode = String.valueOf(Objects.hash(user,
                        transactionCsv.getTransactionDate(),
                        transactionCsv.getAmount(),
                        transactionCsv.getDescription().trim().toLowerCase(),
                        label));

                if (isDuplicate(hashCode)) {
                    failedTransactions.add(new FailedTransaction(i + 1, "Transaction already exists"));
                    continue;
                }
                Transaction transaction = new Transaction(user, hashCode, group, label, createdAt,
                        transactionCsv.getAmount(),
                        transactionCsv.getDescription(),
                        transactionCsv.getTransactionDate(),
                        type);
                successfulTransactions.add(transaction);
                usedHashcodes.add(hashCode);
            }
            return new TransactionParseResult(successfulTransactions, failedTransactions);
        }
    }

    private Optional<Integer> getType(String type) {
        Optional<Integer> result;
        if (type.equalsIgnoreCase("income"))
            result = Optional.of(1);
        else if (type.equalsIgnoreCase("expense"))
            result = Optional.of(2);
        else
            result = Optional.empty();
        return result;
    }

    private boolean isDuplicate(String hashCode) {
        return usedHashcodes.contains(hashCode) || transactionRepository.findByHashcode(hashCode).isPresent();
    }
}
