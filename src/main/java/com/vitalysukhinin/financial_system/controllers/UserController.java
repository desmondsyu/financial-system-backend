package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.dto.ForgotPassword;
import com.vitalysukhinin.financial_system.dto.ResetPassword;
import com.vitalysukhinin.financial_system.dto.UserRequest;
import com.vitalysukhinin.financial_system.dto.UserVerification;
import com.vitalysukhinin.financial_system.entities.TempUser;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TempResetUserRepository;
import com.vitalysukhinin.financial_system.repositories.TempUserRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import com.vitalysukhinin.financial_system.services.EmailService;
import com.vitalysukhinin.financial_system.services.PasswordService;
import com.vitalysukhinin.financial_system.services.VerificationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private PasswordService passwordService;
    private VerificationService verificationService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TempUserRepository tempUserRepository;
    private EmailService emailService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, TempUserRepository tempUserRepository, EmailService emailService, VerificationService verificationService, TempResetUserRepository tempResetUserRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tempUserRepository = tempUserRepository;
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.passwordService = passwordService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserRequest user) {
        String token = generateRandomRegisterToken();
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

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPassword forgotPassword) {
        String token = generateRandomForgotToken();
        passwordService.forgotPassword(forgotPassword.email(), token);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword, @RequestParam String token) {
        if (passwordService.resetPassword(resetPassword.password(), token))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("Invalid or expired token");
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping(path = "/users")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isPresent())
            userRepository.delete(user.get());
        else
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    private String generateRandomRegisterToken() {
        return RandomStringUtils.randomNumeric(6);
    }

    private String generateRandomForgotToken() {
        return RandomStringUtils.randomAlphanumeric(20);
    }
}
