package com.cocoland.repository;

import com.cocoland.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByNameContainingIgnoreCaseOrPhoneContaining(String name, String phone);

    @Query("SELECT COUNT(c) FROM Customer c")
    long countAll();
}
