package com.cocoland.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "thong_so_do")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_do")
    private Integer id;

    @Column(name = "ma_khach")
    private Integer customerId;

    @Column(name = "ma_loai")
    private Integer productTypeId;

    @Column(name = "ma_thong_so")
    private Integer measurementTypeId;

    @Column(name = "ma_ct")
    private Integer orderDetailId;

    @Column(name = "gia_tri")
    private Double value;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String note;

    // Thêm cho API response
    @Transient
    private String measurementTypeName;
    @Transient
    private String unit;
}
