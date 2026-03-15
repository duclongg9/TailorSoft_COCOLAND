package com.cocoland.controller;

import com.cocoland.entity.Measurement;
import com.cocoland.repository.MeasurementRepository;
import com.cocoland.repository.MeasurementTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementRepository measurementRepo;
    private final MeasurementTypeRepository measurementTypeRepo;

    @GetMapping
    public List<Measurement> list(
            @RequestParam(name = "customerId", required = false) Integer customerId,
            @RequestParam(name = "orderDetailId", required = false) Integer orderDetailId) {

        List<Measurement> items;
        if (orderDetailId != null) {
            items = measurementRepo.findByOrderDetailId(orderDetailId);
        } else if (customerId != null) {
            items = measurementRepo.findByCustomerId(customerId);
        } else {
            items = measurementRepo.findAll();
        }

        // Enrich với tên + đơn vị thông số đo
        for (Measurement m : items) {
            if (m.getMeasurementTypeId() != null) {
                measurementTypeRepo.findById(m.getMeasurementTypeId()).ifPresent(mt -> {
                    m.setMeasurementTypeName(mt.getName());
                    m.setUnit(mt.getUnit());
                });
            }
        }
        return items;
    }

    @PostMapping
    public Measurement create(@RequestBody Measurement measurement) {
        return measurementRepo.save(measurement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Measurement> update(@PathVariable("id") Integer id, @RequestBody Measurement body) {
        return measurementRepo.findById(id).map(m -> {
            m.setValue(body.getValue());
            m.setNote(body.getNote());
            return ResponseEntity.ok(measurementRepo.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!measurementRepo.existsById(id)) return ResponseEntity.notFound().build();
        measurementRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
