package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.entities.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface TransactionParser {
    List<Transaction> parse(InputStream input, String email) throws IOException;
}
