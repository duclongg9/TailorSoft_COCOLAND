package com.cocoland.controller;

import com.cocoland.entity.Material;
import com.cocoland.repository.MaterialRepository;
import com.cocoland.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialRepository materialRepo;
    private final FileService fileService;

    @GetMapping
    public List<Material> list() {
        return materialRepo.findAll();
    }

    @GetMapping("/low-stock")
    public List<Material> lowStock() {
        return materialRepo.findLowStock();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> get(@PathVariable("id") Integer id) {
        return materialRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Material create(@RequestBody Material material) {
        return materialRepo.save(material);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> update(@PathVariable("id") Integer id, @RequestBody Material body) {
        return materialRepo.findById(id).map(m -> {
            m.setName(body.getName());
            m.setColor(body.getColor());
            m.setRoll(body.getRoll());
            m.setOrigin(body.getOrigin());
            m.setPrice(body.getPrice());
            m.setQuantity(body.getQuantity());
            m.setUnit(body.getUnit());
            return ResponseEntity.ok(materialRepo.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Material> uploadImage(@PathVariable("id") Integer id,
                                                @RequestParam("file") MultipartFile file) throws IOException {
        return materialRepo.findById(id).map(m -> {
            try {
                fileService.delete(m.getImageUrl());
                m.setImageUrl(fileService.save(file));
                return ResponseEntity.ok(materialRepo.save(m));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!materialRepo.existsById(id)) return ResponseEntity.notFound().build();
        materialRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
