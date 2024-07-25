package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionSearchFilter {
    private static UserRepository userRepository ;

    public TransactionSearchFilter(UserRepository userRepository) {
        TransactionSearchFilter.userRepository = userRepository;
    }

    public static Specification<Transaction> filters(String name, Date from, Date to, String label, Integer type, String group) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(builder.equal(root.get("user_id"), userRepository.findByUsername(name).get().getId()));
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
                predicates.add(builder.equal(root.get("group"), group));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
