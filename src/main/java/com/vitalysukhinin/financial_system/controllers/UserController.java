package com.vitalysukhinin.financial_system.controllers;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.vitalysukhinin.financial_system.dto.UserRequest;
import com.vitalysukhinin.financial_system.dto.UserVerification;
import com.vitalysukhinin.financial_system.entities.TempUser;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TempUserRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import com.vitalysukhinin.financial_system.services.EmailService;
import com.vitalysukhinin.financial_system.services.VerificationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UserController {

    private VerificationService verificationService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TempUserRepository tempUserRepository;
    private EmailService emailService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, TempUserRepository tempUserRepository, EmailService emailService, VerificationService verificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tempUserRepository = tempUserRepository;
        this.emailService = emailService;
        this.verificationService = verificationService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserRequest user) {
        String token = generateRandomToken();
        TempUser tempUser = new TempUser(user.email(), token, LocalDateTime.now().plusMinutes(20), user.username(), passwordEncoder.encode(user.password()),
                user.mStatus(), user.dob());
        tempUserRepository.save(tempUser);
        emailService.sendVerificationCode(user.email(), token);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<String> verifyUser(@RequestBody UserVerification user) {
        if (verificationService.verifyEmail(user.email(), user.token()))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("Invalid or expired verification code");
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    private String generateRandomToken() {
        return RandomStringUtils.randomNumeric(6);
    }
}
