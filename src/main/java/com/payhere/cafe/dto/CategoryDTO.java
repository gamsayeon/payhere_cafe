package com.payhere.cafe.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    @NotNull
    private String name;
}
