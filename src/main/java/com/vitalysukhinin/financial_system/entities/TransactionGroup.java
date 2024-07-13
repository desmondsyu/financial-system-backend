package com.vitalysukhinin.financial_system.entities;

public class TransactionGroup {
    private Integer id;
    private String name;
    private TransactionType transactionType;
    private User user;

    public TransactionGroup(Integer id, String name, TransactionType transactionType, User user) {
        this.id = id;
        this.name = name;
        this.transactionType = transactionType;
        this.user = user;
    }

    public TransactionGroup() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
