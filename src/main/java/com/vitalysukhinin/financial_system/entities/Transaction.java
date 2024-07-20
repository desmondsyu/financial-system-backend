package com.vitalysukhinin.financial_system.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tran_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String hashcode;

    @ManyToOne
    @JoinColumn(name = "tran_group_id")
    private TransactionGroup transactionGroup;

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    private Double amount;

    @Column(name = "customer_notes")
    private String description;
    private Double balance;

    public Transaction(Integer id, User user, String hashcode, TransactionGroup transactionGroup, Label label,
                       LocalDateTime createdAt, Double amount, String description, Double balance, LocalDateTime transactionDate) {
        this.id = id;
        this.user = user;
        this.hashcode = hashcode;
        this.transactionGroup = transactionGroup;
        this.label = label;
        this.createdAt = createdAt;
        this.amount = amount;
        this.description = description;
        this.balance = balance;
        this.transactionDate = transactionDate;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.createdAt = created_at;
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

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transaction_date) {
        this.transactionDate = transaction_date;
    }
}
