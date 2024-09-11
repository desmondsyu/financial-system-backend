package com.vitalysukhinin.financial_system.dto;

import com.vitalysukhinin.financial_system.components.TransactionFrequency;

import java.time.LocalDate;

public record RecurringTransactionResponse(Integer id,
                                           TransactionResponse transaction,
                                           TransactionFrequency frequency,
                                           LocalDate endDate) {
}
