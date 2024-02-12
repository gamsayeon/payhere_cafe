package com.payhere.cafe.model;


import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private Meta meta;
    private T data;

    @Data
    @Builder
    public static class Meta {
        private int code;
        private String message;
    }
}