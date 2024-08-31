package com.vitalysukhinin.financial_system.components;

import com.vitalysukhinin.financial_system.entities.Transaction;

import java.util.List;

public record TransactionParseResult(List<Transaction> successfulTransactions,
                                     List<FailedTransaction> failedTransactions) {
}
