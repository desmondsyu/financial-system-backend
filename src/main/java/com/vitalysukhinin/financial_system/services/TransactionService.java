package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.dto.TransactionGroupResponse;
import com.vitalysukhinin.financial_system.dto.TransactionResponse;
import com.vitalysukhinin.financial_system.dto.UserSimple;
import com.vitalysukhinin.financial_system.entities.Label;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TransactionRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private EntityManager entityManager;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionResponse> response = transactions.stream().map(transaction ->
                new TransactionResponse(
                        new UserSimple(transaction.getUser().getUsername()),
                        transaction.getHashcode(),
                        new TransactionGroupResponse(
                                transaction.getTransactionGroup().getId(),
                                transaction.getTransactionGroup().getName(),
                                transaction.getTransactionGroup().getTransactionType(),
                                new UserSimple(transaction.getUser().getUsername())
                        ),
                        transaction.getLabel(),
                        transaction.getTransactionDate(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getBalance()
                )).toList();
        return response;
    }

    public Optional<Transaction> addTransaction(Transaction transaction) {
        Optional<Transaction> result = Optional.empty();
        Optional<User> optionalUser = userRepository.findByUsername(transaction.getUser().getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            transaction.setUser(user);
            transaction.getTransactionGroup().setUser(user);
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setHashcode(
                    String.valueOf(Objects.hash(transaction.getUser(),
                            transaction.getTransactionDate(),
                            transaction.getCreatedAt(),
                            transaction.getAmount(),
                            transaction.getDescription(),
                            transaction.getLabel()))
            );
            Transaction savedTransaction = transactionRepository.save(transaction);
            result = Optional.of(savedTransaction);
        }
        return result;
    }
    public List<Transaction> getTransactionsWithCriteria(User user, Optional<Date> from, Optional<Date> to, Optional<Label> label, Optional<TransactionGroupResponse> transactionGroupID) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);
        Root<Transaction> transactionRoot = criteriaQuery.from(Transaction.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(transactionRoot.get("user"), user.getId()));
        label.ifPresent(value -> predicates.add(criteriaBuilder.equal(transactionRoot.get("label"), value.getId())));
        from.ifPresent(date -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(transactionRoot.get("transactionDate"), date)));
        transactionGroupID.ifPresent(transaction -> predicates.add(criteriaBuilder.equal(transactionRoot.get("transactionGroup"), transactionGroupID.get().getId())));
        to.ifPresent(date -> predicates.add(criteriaBuilder.lessThanOrEqualTo(transactionRoot.get("transactionDate"), date)));

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Transaction> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }
}
