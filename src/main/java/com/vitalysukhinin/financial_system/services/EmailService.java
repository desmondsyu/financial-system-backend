package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.dto.FeedbackRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender mailSender;
    private static final String DEV_EMAIL = "applicationfinance4@gmail.com";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String to, String code) {
        sendEmail(to,
                "Email verification",
                "Your verification code for financial system is " + code);
    }

    public void sendForgotPasswordCode(String to, String code) {
        sendEmail(to,
                "Reset password",
                "To reset password for financial system use following link: " +
                        "http://localhost:3000/reset-password?token=" + code);
    }

    public void sendFeedbackEmail(FeedbackRequest feedback) {
        sendEmail(DEV_EMAIL,
                feedback.subject(),
                "User with email: " + feedback.userEmail() + "\n" +
                        "Submitted a feedback with following message: \n" +
                        feedback.message()
        );
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
