package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.entities.TempUser;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.TempUserRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationService {

    private UserRepository userRepository;
    private TempUserRepository tempUserRepository;

    public VerificationService(TempUserRepository tempUserRepository, UserRepository userRepository) {
        this.tempUserRepository = tempUserRepository;
        this.userRepository = userRepository;
    }

    public boolean verifyEmail(String email, String token) {
        Optional<TempUser> emailVerification = tempUserRepository.findByEmail(email);
        if (emailVerification.isPresent()) {
            TempUser verification = emailVerification.get();
            if (verification.getExpiryDate().isAfter(LocalDateTime.now()) && verification.getToken().equals(token)) {
                User user = new User();
                user.setEmail(email);
                user.setDob(verification.getDob());
                user.setActive(true);
                user.setGender(verification.getGender());
                user.setUsername(verification.getUsername());
                user.setPassword(verification.getPassword());
                tempUserRepository.delete(verification);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

}
