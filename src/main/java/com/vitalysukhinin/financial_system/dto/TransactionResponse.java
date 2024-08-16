package com.vitalysukhinin.financial_system.dto;

import com.vitalysukhinin.financial_system.entities.Label;

import java.time.LocalDateTime;

public class TransactionResponse {
    private Integer id;
    private UserSimple user;
    private String hashcode;
    private TransactionGroupResponse transactionGroupResponse;
    private LabelResponse label;
    private LocalDateTime transactionDate;
    private Double amount;
    private String description;
    private Double balance;

    public TransactionResponse(Integer id, UserSimple user, String hashcode, TransactionGroupResponse transactionGroupResponse, LabelResponse label, LocalDateTime transactionDate, Double amount, String description, Double balance) {
        this.id = id;
        this.user = user;
        this.hashcode = hashcode;
        this.transactionGroupResponse = transactionGroupResponse;
        this.label = label;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.description = description;
        this.balance = balance;
    }

    public TransactionResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserSimple getUser() {
        return user;
    }

    public void setUser(UserSimple user) {
        this.user = user;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public TransactionGroupResponse getTransactionGroup() {
        return transactionGroupResponse;
    }

    public void setTransactionGroup(TransactionGroupResponse transactionGroupResponse) {
        this.transactionGroupResponse = transactionGroupResponse;
    }

    public LabelResponse getLabel() {
        return label;
    }

    public void setLabel(LabelResponse label) {
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
