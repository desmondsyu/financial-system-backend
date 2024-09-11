package com.vitalysukhinin.financial_system.entities;

import com.vitalysukhinin.financial_system.components.TransactionFrequency;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "recurring_transaction")
public class RecurringTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "tran_id")
    private Transaction transaction;
    @Enumerated(EnumType.STRING)
    private TransactionFrequency frequency;
    private LocalDate endDate;

    public RecurringTransaction(Transaction transaction, TransactionFrequency frequency, LocalDate endDate) {
        this.transaction = transaction;
        this.frequency = frequency;
        this.endDate = endDate;
    }

    public RecurringTransaction() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(TransactionFrequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
