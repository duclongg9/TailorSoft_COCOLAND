package com.cocoland.controller;

import com.cocoland.entity.Customer;
import com.cocoland.repository.CustomerRepository;
import com.cocoland.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepo;
    private final OrderRepository orderRepo;

    @GetMapping
    public List<Customer> list(@RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return customerRepo.findByNameContainingIgnoreCaseOrPhoneContaining(search, search);
        }
        return customerRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable Integer id) {
        return customerRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerRepo.save(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Integer id, @RequestBody Customer body) {
        return customerRepo.findById(id).map(c -> {
            c.setName(body.getName());
            c.setPhone(body.getPhone());
            c.setEmail(body.getEmail());
            c.setAddress(body.getAddress());
            return ResponseEntity.ok(customerRepo.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!customerRepo.existsById(id)) return ResponseEntity.notFound().build();
        customerRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<?>> customerOrders(@PathVariable Integer id) {
        if (!customerRepo.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(orderRepo.findByCustomerId(id));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> stats(@PathVariable Integer id) {
        if (!customerRepo.existsById(id)) return ResponseEntity.notFound().build();
        var orders = orderRepo.findByCustomerId(id);
        long pending = orders.stream().filter(o -> "Dang may".equals(o.getStatus())).count();
        return ResponseEntity.ok(Map.of("total", orders.size(), "pending", pending));
    }
}
