package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.configs.CustomUserDetailsService;
import com.vitalysukhinin.financial_system.dto.CustomPage;
import com.vitalysukhinin.financial_system.dto.TransactionParseResultResponse;
import com.vitalysukhinin.financial_system.dto.TransactionResponse;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.services.TransactionService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private static final Logger logger = Logger.getLogger(TransactionController.class.getName());

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody Transaction transaction) {
        Optional<Transaction> result = transactionService.addTransaction(transaction);
        if (result.isPresent()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping
    public ResponseEntity<CustomPage<TransactionResponse>> getTransactions(
            @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam (required = false) String label,
            @RequestParam (required = false) Integer type,
            @RequestParam (required = false) String group,
            @RequestParam (required = false) Boolean all,
            Authentication authentication,
            Pageable pageable)
    {
        System.out.println(authentication.getName());
        CustomPage<TransactionResponse> result = transactionService.getTransactions(
                authentication.getName(), from, to ,label, type, group, all, pageable
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "pdf")
    public ResponseEntity<byte[]> getPdfFile(
            @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam (required = false) String label,
            @RequestParam (required = false) Integer type,
            @RequestParam (required = false) String group,
            Authentication authentication)
    {

        byte[] pdfBytes = transactionService.generateUserTransactionPdf(authentication.getName(), from, to, label, type, group);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "user_transactions.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @PostMapping(path = "/parse")
    public ResponseEntity<TransactionParseResultResponse> parseTransactions(@RequestParam("file") MultipartFile file, Authentication authentication) {
        try {
            logger.info(authentication.getName());
            logger.info(file.getOriginalFilename());
            TransactionParseResultResponse result = transactionService.parseTransactions(file, authentication.getName());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction) {
        Optional<Transaction> result = transactionService.updateTransaction(transaction);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}
