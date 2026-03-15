package com.cocoland.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "kho_vai")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_vai")
    private Integer id;

    @Column(name = "ten_vai")
    private String name;

    @Column(name = "mau_sac")
    private String color;

    @Column(name = "quyen_vai")
    private String roll;

    @Column(name = "xuat_xu")
    private String origin;

    @Column(name = "gia_thanh")
    private Double price;

    @Column(name = "so_luong")
    private Double quantity;

    @Column(name = "don_vi")
    private String unit;

    @Column(name = "hinh_hoa_don")
    private String invoiceImage;

    @Column(name = "hinh_vai")
    private String imageUrl;
}
