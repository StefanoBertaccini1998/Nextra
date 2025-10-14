package com.nextra.app.controller;

import com.nextra.app.model.Variant;
import com.nextra.app.repository.VariantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variants")
public class VariantController {

    private final VariantRepository variantRepository;

    public VariantController(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    @GetMapping
    public List<Variant> getAll() {
        return variantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Variant> getById(@PathVariable Long id) {
        return variantRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Variant> create(@RequestBody Variant variant) {
        return ResponseEntity.ok(variantRepository.save(variant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Variant> update(@PathVariable Long id, @RequestBody Variant updated) {
        return variantRepository.findById(id)
            .map(existing -> {
                existing.setColor(updated.getColor());
                existing.setFabric(updated.getFabric());
                existing.setOptional(updated.getOptional());
                existing.setPriceDelta(updated.getPriceDelta());
                existing.setCustomFields(updated.getCustomFields());
                return ResponseEntity.ok(variantRepository.save(existing));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (variantRepository.existsById(id)) {
            variantRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
