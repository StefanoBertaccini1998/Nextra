package com.nextra.app.controller;

import com.nextra.app.model.SofaFrame;
import com.nextra.app.repository.SofaFrameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/frames")
public class SofaFrameController {

    private final SofaFrameRepository sofaFrameRepository;

    public SofaFrameController(SofaFrameRepository sofaFrameRepository) {
        this.sofaFrameRepository = sofaFrameRepository;
    }

    @GetMapping
    public List<SofaFrame> getAll() {
        return sofaFrameRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SofaFrame> getById(@PathVariable Long id) {
        return sofaFrameRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SofaFrame> create(@RequestBody SofaFrame frame) {
        return ResponseEntity.ok(sofaFrameRepository.save(frame));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SofaFrame> update(@PathVariable Long id, @RequestBody SofaFrame updated) {
        return sofaFrameRepository.findById(id)
            .map(existing -> {
                existing.setName(updated.getName());
                existing.setMaterial(updated.getMaterial());
                existing.setCustomFields(updated.getCustomFields());
                return ResponseEntity.ok(sofaFrameRepository.save(existing));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (sofaFrameRepository.existsById(id)) {
            sofaFrameRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
