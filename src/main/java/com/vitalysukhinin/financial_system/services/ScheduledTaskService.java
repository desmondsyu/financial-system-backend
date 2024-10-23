package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.entities.RecurringTransaction;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.repositories.RecurringTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ScheduledTaskService {

    private final RecurringTransactionService recurringTransactionService;
    private final TransactionService transactionService;
    private final RecurringTransactionRepository recurringTransactionRepository;
    private final Logger logger = Logger.getLogger(ScheduledTaskService.class.getName());

    public ScheduledTaskService(RecurringTransactionService recurringTransactionService, TransactionService transactionService, RecurringTransactionRepository recurringTransactionRepository) {
        this.recurringTransactionService = recurringTransactionService;
        this.transactionService = transactionService;
        this.recurringTransactionRepository = recurringTransactionRepository;
    }

    // Every 1 minute
    @Scheduled(fixedRate = 1 * 60 * 1000)
    @Transactional
    public void checkAndProcessRecurringTransactions() {
        List<RecurringTransaction> recurringTransactions = recurringTransactionService.getAll();
        for (RecurringTransaction recurringTransaction : recurringTransactions) {
            LocalDateTime now = LocalDateTime.now();
            if (isTimeToRepeat(recurringTransaction, now)) {
                Transaction transaction = recurringTransaction.getTransaction();
                transaction.setId(null);
                transaction.setTransactionDate(now);
                transaction.setHashcode(transactionService.generateHashcode(transaction));
                Optional<Transaction> savedTransaction = transactionService.addTransaction(transaction);
                if (savedTransaction.isPresent()) {
                    recurringTransaction.setTransaction(savedTransaction.get());
                    recurringTransactionRepository.save(recurringTransaction);
                }
            }
        }
    }

    private boolean isTimeToRepeat(RecurringTransaction rt, LocalDateTime now) {
        return switch (rt.getFrequency()) {
            case DAILY ->
                    rt.getTransaction().getTransactionDate().plusDays(1).isBefore(now) && isEnded(rt.getEndDate(), now);
            case WEEKLY ->
                    rt.getTransaction().getTransactionDate().plusWeeks(1).isBefore(now) && isEnded(rt.getEndDate(), now);
            case MONTHLY ->
                    rt.getTransaction().getTransactionDate().plusMonths(1).isBefore(now) && isEnded(rt.getEndDate(), now);
            case YEARLY ->
                    rt.getTransaction().getTransactionDate().plusYears(1).isBefore(now) && isEnded(rt.getEndDate(), now);
            default -> false;
        };
    }

    private boolean isEnded(LocalDate now, LocalDateTime end) {
        return end == null || end.isBefore(now.atStartOfDay());
    }
}
