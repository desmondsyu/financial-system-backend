package com.vitalysukhinin.financial_system.dto;

import com.vitalysukhinin.financial_system.entities.Label;
import com.vitalysukhinin.financial_system.entities.User;

import java.sql.Date;
import java.util.Optional;

public class TransactionSearchFilter {

    private User user;
    private Optional<Date> from = Optional.empty();
    private Optional<Date> to = Optional.empty();
    private Optional<TransactionGroupResponse> transactionGroupResponse = Optional.empty();
    private Optional<Label> label = Optional.empty();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Optional<Label> getLabel() {
        return label;
    }

    public void setLabel(Optional<Label> label) {
        this.label = label;
    }

    public Optional<TransactionGroupResponse> getTransactionGroupResponse() {
        return transactionGroupResponse;
    }

    public void setTransactionGroupResponse(Optional<TransactionGroupResponse> transactionGroupResponse) {
        this.transactionGroupResponse = transactionGroupResponse;
    }

    public Optional<Date> getTo() {
        return to;
    }

    public void setTo(Optional<Date> to) {
        this.to = to;
    }

    public Optional<Date> getFrom() {
        return from;
    }

    public void setFrom(Optional<Date> from) {
        this.from = from;
    }
}
