package com.cocoland.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "loai_san_pham")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_loai")
    private Integer id;

    @Column(name = "ten_loai", nullable = false)
    private String name;

    @Column(name = "ky_hieu")
    private String code;

    @Column(name = "hinh_anh")
    private String imageUrl;
}
