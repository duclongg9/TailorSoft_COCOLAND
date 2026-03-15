package com.cocoland.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "chi_tiet_don")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ct")
    private Integer id;

    @Column(name = "ma_don")
    private Integer orderId;

    @Column(name = "loai_sp")
    private String productType;

    @Column(name = "ma_vai")
    private Integer materialId;

    @Column(name = "ten_vai")
    private String materialName;

    @Column(name = "don_gia")
    private Double unitPrice;

    @Column(name = "so_luong")
    private Integer quantity;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String note;
}
