package com.payhere.cafe.dto;

import com.payhere.cafe.enums.ProductSize;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotNull
    private String category;

    @NotNull
    private int price;

    @NotNull
    private int cost;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String barcode;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private ProductSize size;
}
