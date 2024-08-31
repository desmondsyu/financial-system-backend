package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.dto.LabelResponse;
import com.vitalysukhinin.financial_system.dto.TransactionGroupResponse;
import com.vitalysukhinin.financial_system.dto.TransactionResponse;
import com.vitalysukhinin.financial_system.dto.UserSimple;
import com.vitalysukhinin.financial_system.entities.Transaction;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TransactionConverterService {

    public Function<Transaction, TransactionResponse> convertTransactionToResponse() {
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
                transaction.getBalance(),
                transaction.getType()
        );
    }
}
