package com.cocoland.repository;

import com.cocoland.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query("SELECT m FROM Material m WHERE m.quantity <= 20 ORDER BY m.quantity ASC")
    List<Material> findLowStock();

    @Query("SELECT COUNT(m) FROM Material m WHERE m.quantity <= 20")
    long countLowStock();
}
