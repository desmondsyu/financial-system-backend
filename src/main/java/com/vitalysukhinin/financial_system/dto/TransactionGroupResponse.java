package com.vitalysukhinin.financial_system.dto;

import com.vitalysukhinin.financial_system.entities.TransactionType;

public class TransactionGroupResponse {
    private Integer id;
    private String name;
    private TransactionType transactionType;
    private UserSimple user;

    public TransactionGroupResponse(Integer id, String name, TransactionType transactionType, UserSimple user) {
        this.id = id;
        this.name = name;
        this.transactionType = transactionType;
        this.user = user;
    }

    public TransactionGroupResponse() {
    }

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

    public UserSimple getUser() {
        return user;
    }

    public void setUser(UserSimple user) {
        this.user = user;
    }
}
