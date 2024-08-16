package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.entities.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionSearchFilter {

    public static Specification<Transaction> filters(User user, LocalDateTime from, LocalDateTime to, Label label, TransactionType type, TransactionGroup group) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (user != null) {
                predicates.add(builder.equal(root.get("user"), user));
            }
            if (from != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("transactionDate"),from));
            }
            if (to != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("transactionDate"),to));
            }
            if (label != null) {
                predicates.add(builder.equal(root.get("label"), label));
            }
            if (type != null) {
                predicates.add(builder.equal(root.get("type"), type));
            }
            if (group != null) {
                predicates.add(builder.equal(root.get("transactionGroup"), group));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
