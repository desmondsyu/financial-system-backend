package com.vitalysukhinin.financial_system.entities;

public class TransactionType {

    private Integer id;
    private String name;

    public TransactionType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public TransactionType() {
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
}
