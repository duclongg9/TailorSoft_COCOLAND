package com.cocoland.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "khach_hang")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_khach")
    private Integer id;

    @Column(name = "ho_ten")
    private String name;

    @Column(name = "so_dien_thoai")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String address;

    @Column(name = "gioi_tinh")
    private String gender;

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime createdAt;
}
