package com.cocoland.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "don_hang")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_don")
    private Integer id;

    @Column(name = "ma_khach")
    private Integer customerId;

    @Column(name = "ngay_dat")
    private LocalDate orderDate;

    @Column(name = "ngay_giao")
    private LocalDate deliveryDate;

    // "Dang may" | "Hoan thanh" | "Don huy"
    @Column(name = "trang_thai")
    private String status;

    @Column(name = "tong_tien")
    private Double total;

    @Column(name = "da_coc")
    private Double deposit;

    @Column(name = "anh_coc")
    private String depositImage;

    @Column(name = "anh_full")
    private String fullImage;

    // Không lưu DB — nạp từ join khi trả về API
    @Transient
    private String customerName;
    @Transient
    private String customerPhone;
    @Transient
    private String customerEmail;
    @Transient
    private String customerAddress;
}
