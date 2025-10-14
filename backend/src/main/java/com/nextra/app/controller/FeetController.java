package com.nextra.app.controller;

import com.nextra.app.model.Feet;
import com.nextra.app.repository.FeetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feet")
public class FeetController {

    private final FeetRepository feetRepository;

    public FeetController(FeetRepository feetRepository) {
        this.feetRepository = feetRepository;
    }

    @GetMapping
    public List<Feet> getAll() {
        return feetRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feet> getById(@PathVariable Long id) {
        return feetRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Feet> create(@RequestBody Feet feet) {
        return ResponseEntity.ok(feetRepository.save(feet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feet> update(@PathVariable Long id, @RequestBody Feet updated) {
        return feetRepository.findById(id)
            .map(existing -> {
                existing.setName(updated.getName());
                existing.setMaterial(updated.getMaterial());
                existing.setHeightCm(updated.getHeightCm());
                existing.setCustomFields(updated.getCustomFields());
                return ResponseEntity.ok(feetRepository.save(existing));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (feetRepository.existsById(id)) {
            feetRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
