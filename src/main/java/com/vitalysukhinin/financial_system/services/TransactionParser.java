package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.components.TransactionParseResult;
import com.vitalysukhinin.financial_system.entities.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface TransactionParser {
    TransactionParseResult parse(InputStream input, String email) throws IOException;
}
