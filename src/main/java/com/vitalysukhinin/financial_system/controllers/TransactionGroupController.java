package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.entities.TransactionGroup;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TransactionGroupRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    //TODO Add dto to hide user data
    @GetMapping
    public ResponseEntity<List<TransactionGroup>> getAllTransactionGroups(Authentication auth) {
        Optional<User> user = userRepository.findByEmail(auth.getName());
        if (user.isPresent())
            return ResponseEntity.ok(transactionGroupRepository.findByUserOrUserIsNull(user.get()));
         else
             return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TransactionGroup> createTransactionGroup(@RequestBody TransactionGroup transactionGroup) {

        Optional<User> user = userRepository.findByEmail(transactionGroup.getUser().getEmail());
        if (user.isPresent()) {
            transactionGroup.setUser(user.get());
            TransactionGroup savedTransactionGroup = transactionGroupRepository.save(transactionGroup);
            return ResponseEntity.ok(savedTransactionGroup);
        }
        return ResponseEntity.badRequest().build();
    }
}
