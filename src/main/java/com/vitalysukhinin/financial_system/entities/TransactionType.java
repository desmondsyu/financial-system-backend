package com.vitalysukhinin.financial_system.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "type")
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "transactionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TransactionGroup> transactionGroups;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> transactions;

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
