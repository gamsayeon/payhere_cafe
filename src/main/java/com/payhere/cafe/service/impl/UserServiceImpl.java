package com.payhere.cafe.service.impl;

import com.payhere.cafe.dto.UserDTO;
import com.payhere.cafe.exception.CafeServerException;
import com.payhere.cafe.exception.DBConnectionException;
import com.payhere.cafe.model.User;
import com.payhere.cafe.modelmapper.UserModelMapper;
import com.payhere.cafe.repository.UserRepository;
import com.payhere.cafe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserModelMapper userMapper;
    private final UserRepository userRepository;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public UserDTO signUp(UserDTO userDTO) {
        try {
            User user = userMapper.convertToEntity(userDTO);
            User resultUser = userRepository.save(user);

            UserDTO resultUserDTO = userMapper.convertToDTO(resultUser);
            logger.info("유저 " + resultUser.getPhoneNumber() + "을 회원가입에 성공했습니다.");
            return resultUserDTO;
        } catch (DataAccessException ex) {
            logger.error("데이터베이스 연결 예외 발생: " + ex.getMessage());
            throw new DBConnectionException("데이터베이스 연결 예외 발생", ex);
        } catch (Exception ex) {
            logger.error("예외 발생: " + ex.getMessage());
            throw new CafeServerException("서버 오류 발생", ex);
        }
    }
}
