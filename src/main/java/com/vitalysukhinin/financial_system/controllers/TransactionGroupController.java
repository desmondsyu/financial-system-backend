package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.entities.TransactionGroup;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TransactionGroupRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction-groups")
public class TransactionGroupController {

    private final TransactionGroupRepository transactionGroupRepository;
    private final UserRepository userRepository;

    public TransactionGroupController(TransactionGroupRepository transactionGroupRepository, UserRepository userRepository) {
        this.transactionGroupRepository = transactionGroupRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<TransactionGroup>> getAllTransactionGroups(@RequestParam Optional<String> username) {
        if (username.isPresent()) {
            Optional<User> user = userRepository.findByUsername(username.get());
            if (user.isPresent())
                return ResponseEntity.ok(transactionGroupRepository.findAllByUser(user.get()));
             else
                 return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(transactionGroupRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<TransactionGroup> createTransactionGroup(@RequestBody TransactionGroup transactionGroup) {

        Optional<User> user = userRepository.findByUsername(transactionGroup.getUser().getUsername());
        if (user.isPresent()) {
            transactionGroup.setUser(user.get());
            TransactionGroup savedTransactionGroup = transactionGroupRepository.save(transactionGroup);
            return ResponseEntity.ok(savedTransactionGroup);
        }
        return ResponseEntity.badRequest().build();
    }
}
