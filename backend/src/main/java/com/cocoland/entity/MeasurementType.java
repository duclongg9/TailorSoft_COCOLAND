package com.cocoland.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "loai_thong_so")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MeasurementType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thong_so")
    private Integer id;

    @Column(name = "ten_thong_so", nullable = false)
    private String name;

    @Column(name = "don_vi")
    private String unit;

    @Column(name = "bo_phan")
    private String bodyPart;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String note;
}
