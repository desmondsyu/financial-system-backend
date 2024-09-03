package com.vitalysukhinin.financial_system.services;

import com.vitalysukhinin.financial_system.dto.FeedbackRequest;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final EmailService emailService;

    public FeedbackService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void submitFeedback(FeedbackRequest feedback) {
        emailService.sendFeedbackEmail(feedback);
    }
}
