package com.vitalysukhinin.financial_system.controllers;

import com.vitalysukhinin.financial_system.entities.Label;
import com.vitalysukhinin.financial_system.entities.User;
import com.vitalysukhinin.financial_system.repositories.LabelRepository;
import com.vitalysukhinin.financial_system.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/labels")
public class LabelController {

    private final LabelRepository labelRepository;
    private final UserRepository userRepository;

    public LabelController(LabelRepository labelRepository, UserRepository userRepository) {
        this.labelRepository = labelRepository;
        this.userRepository = userRepository;
    }

    //TODO Add dto to hide user data
    @GetMapping
    public ResponseEntity<List<Label>> getLabels(Authentication auth) {
        String email = auth.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(labelRepository.findByUserOrUserIsNull(user.get()));
        } else
            return ResponseEntity.notFound().build();
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
