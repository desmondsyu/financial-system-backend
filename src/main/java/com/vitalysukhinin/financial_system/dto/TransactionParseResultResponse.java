package com.vitalysukhinin.financial_system.dto;

import com.vitalysukhinin.financial_system.components.FailedTransaction;

import java.util.List;

public record TransactionParseResultResponse(List<TransactionResponse> successfulTransactions,
                                             List<FailedTransaction> failedTransactions) {
}
