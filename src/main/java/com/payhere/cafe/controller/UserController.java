package com.payhere.cafe.controller;


import com.payhere.cafe.dto.UserDTO;
import com.payhere.cafe.model.CommonResponse;
import com.payhere.cafe.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @PostMapping
    public ResponseEntity<CommonResponse<UserDTO>> signUpUser(@RequestBody @Valid UserDTO userDTO) {
        logger.debug("회원 가입합니다.");
        CommonResponse<UserDTO> successResponse = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                userService.signUp(userDTO));
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/login")
    public ResponseEntity<CommonResponse<String>> login(@RequestBody @Valid UserDTO userDTO) {
        logger.debug("로그인합니다.");
        CommonResponse<String> successResponse = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                userService.login(userDTO));
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<CommonResponse<String>> logout(HttpServletRequest request) {
        logger.debug("로그아웃합니다.");
        CommonResponse<String> successResponse = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                userService.logout(request));
        return ResponseEntity.ok(successResponse);
    }
}
