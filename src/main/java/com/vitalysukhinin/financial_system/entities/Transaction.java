package com.vitalysukhinin.financial_system.entities;

public class Transaction {

    private String name;

    public Transaction(String name) {
        this.name = name;
    }

    public Transaction() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
