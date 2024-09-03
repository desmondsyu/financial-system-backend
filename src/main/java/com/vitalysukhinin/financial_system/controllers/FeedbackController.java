package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.dto.FeedbackRequest;
import com.vitalysukhinin.financial_system.services.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/topics")
    public List<String> getFeedbackTopics() {
        return Arrays.asList("Bug Report", "Feature Request", "General Inquiry");
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        feedbackService.submitFeedback(feedbackRequest);
        return ResponseEntity.ok().build();
    }
}
