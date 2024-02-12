package com.payhere.cafe.service;

import com.payhere.cafe.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    UserDTO signUp(UserDTO userDTO);

    String login(UserDTO userDTO);

    String logout(HttpServletRequest request);
}
