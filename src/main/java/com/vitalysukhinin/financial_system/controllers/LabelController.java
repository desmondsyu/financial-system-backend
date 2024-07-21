package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.entities.Label;
import com.vitalysukhinin.financial_system.repositories.LabelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labels")
public class LabelController {

    private final LabelRepository labelRepository;

    public LabelController(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @GetMapping
    public ResponseEntity<List<Label>> getLabels() {
        return ResponseEntity.ok(labelRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Label> createLabel(@RequestBody Label label) {
        return ResponseEntity.ok(labelRepository.save(label));
    }
}
