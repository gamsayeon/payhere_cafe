package com.payhere.cafe.controller;


import com.payhere.cafe.dto.UserDTO;
import com.payhere.cafe.model.CommonResponse;
import com.payhere.cafe.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @PostMapping
    public ResponseEntity<CommonResponse<UserDTO>> signUpUser(@RequestBody @Valid UserDTO userDTO) {
        logger.debug("회원 가입합니다.");
        CommonResponse<UserDTO> response = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                userService.signUp(userDTO));
        return ResponseEntity.ok(response);
    }
}
