package com.vitalysukhinin.financial_system.components;

import com.vitalysukhinin.financial_system.services.CsvParser;
import com.vitalysukhinin.financial_system.services.TransactionParser;
import org.springframework.stereotype.Component;

@Component
public class ParserFactory {

    private final CsvParser csvParser;

    public ParserFactory(CsvParser csvParser) {
        this.csvParser = csvParser;
    }

    public TransactionParser getTransactionParser(String type) {
        switch (type.toLowerCase()) {
            case "csv":
                return csvParser;
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
    }

}
