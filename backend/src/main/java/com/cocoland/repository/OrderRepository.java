package com.cocoland.repository;

import com.cocoland.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByCustomerId(Integer customerId);

    List<Order> findByStatus(String status);

    @Query("SELECT COUNT(o) FROM Order o")
    long countAll();

    @Query("SELECT o FROM Order o WHERE " +
           "(:status IS NULL OR o.status = :status) " +
           "ORDER BY o.id DESC")
    List<Order> findByStatusOptional(@Param("status") String status);
}
