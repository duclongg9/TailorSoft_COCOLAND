package com.cocoland.controller;

import com.cocoland.repository.CustomerRepository;
import com.cocoland.repository.MaterialRepository;
import com.cocoland.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final CustomerRepository customerRepo;
    private final OrderRepository orderRepo;
    private final MaterialRepository materialRepo;

    @GetMapping
    public Map<String, Object> stats() {
        return Map.of(
                "customerCount", customerRepo.countAll(),
                "orderCount", orderRepo.countAll(),
                "activeOrderCount", orderRepo.findByStatus("Dang may").size(),
                "lowStockCount", materialRepo.countLowStock()
        );
    }
}
