package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.entities.Label;
import com.vitalysukhinin.financial_system.repositories.LabelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

    @GetMapping(path = "/{id}")
    public ResponseEntity<Label> getLabel(@PathVariable Integer id) {
        Optional<Label> found = labelRepository.findById(id);
        if (found.isPresent())
            return ResponseEntity.ok(found.get());
         else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Label> createLabel(@RequestBody Label label) {
        Label createdLabel = labelRepository.save(label);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdLabel.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Integer id) {
        labelRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Label> updateLabel(@RequestBody Label label) {
        return ResponseEntity.ok(labelRepository.save(label));
    }
}
