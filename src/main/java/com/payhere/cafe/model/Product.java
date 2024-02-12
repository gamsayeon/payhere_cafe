package com.payhere.cafe.model;

import com.payhere.cafe.enums.ProductSize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity(name = "product")
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "category")
    private String category;

    @Column(name = "price")
    private int price;

    @Column(name = "cost")
    private int cost;

    @Column(name = "name")
    private String name;

    @Column(name = "choseong_name")
    private String choseongName;

    @Column(name = "description")
    private String description;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "size")
    @Enumerated(EnumType.STRING)
    private ProductSize size;
}
