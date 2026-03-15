package com.cocoland.repository;

import com.cocoland.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    List<Measurement> findByCustomerIdAndProductTypeId(Integer customerId, Integer productTypeId);

    List<Measurement> findByOrderDetailId(Integer orderDetailId);

    List<Measurement> findByCustomerId(Integer customerId);
}
