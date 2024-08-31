package com.vitalysukhinin.financial_system.components;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.vitalysukhinin.financial_system.services.LocalDateTimeConverter;

import java.time.LocalDateTime;

public class TransactionCsv {
    @CsvBindByPosition(position = 0)
    private String transactionGroup;
    @CsvBindByPosition(position = 1)
    private String label;
    @CsvCustomBindByPosition(position = 2, converter = LocalDateTimeConverter.class)
    private LocalDateTime transactionDate;
    @CsvBindByPosition(position = 3)
    private Double amount;
    @CsvBindByPosition(position = 4)
    private String description;
    @CsvBindByPosition(position = 5)
    private String type;

    public String getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(String transactionGroup) {
        this.transactionGroup = transactionGroup;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
