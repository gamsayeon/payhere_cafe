package com.payhere.cafe.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다 (예: 010-1234-5678).")
    private String phoneNumber;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{3,20}$",
            message = "비밀번호는 3자리 이상 20자리 이하로 숫자와 영어, 특수기호(!@#$%^&*)를 각 하나이상 포함해주세요")
    private String password;
}
