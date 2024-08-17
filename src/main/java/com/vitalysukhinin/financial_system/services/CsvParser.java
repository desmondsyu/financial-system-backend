package com.vitalysukhinin.financial_system.services;

import com.opencsv.bean.CsvToBeanBuilder;
import com.vitalysukhinin.financial_system.dto.TransactionCsv;
import com.vitalysukhinin.financial_system.entities.*;
import com.vitalysukhinin.financial_system.repositories.LabelRepository;
import com.vitalysukhinin.financial_system.repositories.TransactionGroupRepository;
import com.vitalysukhinin.financial_system.repositories.TransactionTypeRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CsvParser implements TransactionParser{
    private final TransactionGroupRepository transactionGroupRepository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public CsvParser(TransactionGroupRepository transactionGroupRepository, TransactionTypeRepository transactionTypeRepository, UserRepository userRepository, LabelRepository labelRepository) {
        this.transactionGroupRepository = transactionGroupRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    @Override
    public List<Transaction> parse(InputStream input, String email) throws IOException {
        try (Reader reader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(reader)) {

            List<TransactionCsv> transactionsCsvs = new CsvToBeanBuilder<TransactionCsv>(reader).
                    withType(TransactionCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build()
                    .parse();
            for (TransactionCsv record : transactionsCsvs) {
                System.out.println(record);
            }
            List<Transaction> transactions = new ArrayList<>();
            Optional<User> userOptional = userRepository.findByEmail(email);
            User user;
            if (userOptional.isPresent())
                user = userOptional.get();
            else
                return new ArrayList<>();

            for (TransactionCsv transactionCsv : transactionsCsvs) {
                int typeId;
                if (transactionCsv.getType().equalsIgnoreCase("income"))
                    typeId = 1;
                else if (transactionCsv.getType().equalsIgnoreCase("expense"))
                    typeId = 2;
                else
                    continue;

                Optional<TransactionType> typeOptional = transactionTypeRepository.findById(typeId);
                TransactionType type;
                if (typeOptional.isPresent())
                    type = typeOptional.get();
                else
                    continue;

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
                String hashCode =  String.valueOf(Objects.hash(user,
                        transactionCsv.getTransactionDate(),
                        createdAt,
                        transactionCsv.getAmount(),
                        transactionCsv.getDescription(),
                        label));
                Transaction transaction = new Transaction(user, hashCode, group, label, createdAt,
                        transactionCsv.getAmount(),
                        transactionCsv.getDescription(),
                        transactionCsv.getTransactionDate(),
                        type);
                transactions.add(transaction);
            }
            return transactions;
        }
    }
}
