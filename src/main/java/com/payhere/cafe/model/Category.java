package com.payhere.cafe.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity(name = "category")
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name")
    private String name;
}
