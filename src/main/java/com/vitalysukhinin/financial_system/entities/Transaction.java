package com.vitalysukhinin.financial_system.entities;

import java.time.LocalDateTime;

public class Transaction {

    private Integer id;
    private User user;
    private String hashcode;
    private TransactionGroup transactionGroup;
    private Label label;
    private LocalDateTime created_at;
    private Double amount;
    private String description;
    private Double balance;

    public Transaction(Integer id, User user, String hashcode, TransactionGroup transactionGroup, Label label,
                       LocalDateTime created_at, Double amount, String description, Double balance) {
        this.id = id;
        this.user = user;
        this.hashcode = hashcode;
        this.transactionGroup = transactionGroup;
        this.label = label;
        this.created_at = created_at;
        this.amount = amount;
        this.description = description;
        this.balance = balance;
    }

    public Transaction() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public TransactionGroup getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
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