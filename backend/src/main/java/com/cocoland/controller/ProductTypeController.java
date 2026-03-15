package com.cocoland.controller;

import com.cocoland.entity.ProductType;
import com.cocoland.entity.MeasurementType;
import com.cocoland.repository.ProductTypeRepository;
import com.cocoland.repository.MeasurementTypeRepository;
import com.cocoland.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductTypeController {

    private final ProductTypeRepository productTypeRepo;
    private final MeasurementTypeRepository measurementTypeRepo;
    private final FileService fileService;

    // ---- Product Types ----
    @GetMapping("/api/product-types")
    public List<ProductType> listProductTypes() {
        return productTypeRepo.findAll();
    }

    @PostMapping("/api/product-types")
    public ProductType createProductType(@RequestBody ProductType pt) {
        return productTypeRepo.save(pt);
    }

    @GetMapping("/api/product-types/{id}")
    public ResponseEntity<ProductType> getProductType(@PathVariable Integer id) {
        return productTypeRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/product-types/{id}")
    public ResponseEntity<ProductType> updateProductType(@PathVariable Integer id, @RequestBody ProductType body) {
        return productTypeRepo.findById(id).map(pt -> {
            pt.setName(body.getName());
            pt.setCode(body.getCode());
            if (body.getImageUrl() != null) pt.setImageUrl(body.getImageUrl());
            return ResponseEntity.ok(productTypeRepo.save(pt));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/product-types/{id}/upload-image")
    public ResponseEntity<ProductType> uploadImage(@PathVariable Integer id,
                                                   @RequestParam("file") MultipartFile file) throws IOException {
        return productTypeRepo.findById(id).map(pt -> {
            try {
                fileService.delete(pt.getImageUrl());
                pt.setImageUrl(fileService.save(file));
                return ResponseEntity.ok(productTypeRepo.save(pt));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/product-types/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Integer id) {
        if (!productTypeRepo.existsById(id)) return ResponseEntity.notFound().build();
        productTypeRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Measurement Types ----
    @GetMapping("/api/measurement-types")
    public List<MeasurementType> listMeasurementTypes(
            @RequestParam(required = false) Integer productTypeId) {
        if (productTypeId != null) {
            return measurementTypeRepo.findByProductTypeId(productTypeId);
        }
        return measurementTypeRepo.findAll();
    }

    @PostMapping("/api/measurement-types")
    public MeasurementType createMeasurementType(@RequestBody MeasurementType mt) {
        return measurementTypeRepo.save(mt);
    }

    @PutMapping("/api/measurement-types/{id}")
    public ResponseEntity<MeasurementType> updateMeasurementType(
            @PathVariable Integer id, @RequestBody MeasurementType body) {
        return measurementTypeRepo.findById(id).map(mt -> {
            mt.setName(body.getName());
            mt.setUnit(body.getUnit());
            mt.setBodyPart(body.getBodyPart());
            mt.setNote(body.getNote());
            return ResponseEntity.ok(measurementTypeRepo.save(mt));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/measurement-types/{id}")
    public ResponseEntity<Void> deleteMeasurementType(@PathVariable Integer id) {
        if (!measurementTypeRepo.existsById(id)) return ResponseEntity.notFound().build();
        measurementTypeRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
