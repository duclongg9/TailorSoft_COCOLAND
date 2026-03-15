package com.cocoland.repository;

import com.cocoland.entity.MeasurementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeasurementTypeRepository extends JpaRepository<MeasurementType, Integer> {

    @Query(value = "SELECT lt.* FROM loai_thong_so lt " +
                   "JOIN loai_sp_thong_so lst ON lt.ma_thong_so = lst.ma_thong_so " +
                   "WHERE lst.ma_loai = :productTypeId", nativeQuery = true)
    List<MeasurementType> findByProductTypeId(@Param("productTypeId") Integer productTypeId);
}
