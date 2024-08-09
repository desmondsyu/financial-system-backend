package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.entities.TempResetUser;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TempResetUserRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TempResetUserRepository tempResetUserRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(UserRepository userRepository, EmailService emailService, TempResetUserRepository tempResetUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.tempResetUserRepository = tempResetUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void forgotPassword(String email, String token) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            emailService.sendForgotPasswordCode(email, token);
            TempResetUser resetUser = new TempResetUser(email, token, LocalDateTime.now().plusMinutes(15));
            tempResetUserRepository.save(resetUser);
        }
    }

    public boolean resetPassword(String password, String token) {
        Optional<TempResetUser> resetUser = tempResetUserRepository.findByToken(token);
        if (resetUser.isPresent()) {
            TempResetUser tempResetUser = resetUser.get();
            String email = tempResetUser.getEmail();
            Optional<User> userFound = userRepository.findByEmail(email);
            if (userFound.isPresent() && tempResetUser.getExpiryDate().isAfter(LocalDateTime.now())) {
                User user = userFound.get();
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                tempResetUserRepository.delete(tempResetUser);
                return true;
            }
        }
        return false;
    }
}
