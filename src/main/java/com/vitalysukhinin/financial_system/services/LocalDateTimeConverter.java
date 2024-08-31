package com.vitalysukhinin.financial_system.services;

import com.opencsv.bean.AbstractBeanField;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class LocalDateTimeConverter extends AbstractBeanField<LocalDateTime, String> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    @Override
    protected LocalDateTime convert(String s) {
        try {
            return LocalDateTime.parse(s, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
