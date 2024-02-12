package com.payhere.cafe.modelmapper;

import com.payhere.cafe.dto.UserDTO;
import com.payhere.cafe.model.User;
import com.payhere.cafe.util.Sha256Encrypt;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserModelMapper {
    private final ModelMapper modelMapper;

    public User convertToEntity(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(Sha256Encrypt.encrypt(userDTO.getPassword()));
        return user;
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
}