package com.payhere.cafe.service.impl;

import com.payhere.cafe.dto.UserDTO;
import com.payhere.cafe.exception.CafeServerException;
import com.payhere.cafe.exception.DBConnectionException;
import com.payhere.cafe.exception.NotMatchedJWTPatternException;
import com.payhere.cafe.exception.NotMatchedUsersException;
import com.payhere.cafe.model.User;
import com.payhere.cafe.modelmapper.UserModelMapper;
import com.payhere.cafe.repository.UserRepository;
import com.payhere.cafe.service.UserService;
import com.payhere.cafe.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtTokenUtil jwtTokenUtil;
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

    @Override
    public String login(UserDTO userDTO) {
        try {
            User user = userMapper.convertToEntity(userDTO);
            user = userRepository.findByPhoneNumberAndPassword(user.getPhoneNumber(), user.getPassword());
            if (user == null) {
                logger.warn("해당 사용자가 없습니다.");
                throw new NotMatchedUsersException("해당 사용자가 없습니다.", userDTO);
            }
            String jwtToken = jwtTokenUtil.generateToken(user.getPhoneNumber());
            return jwtToken;
        } catch (Exception ex) {
            logger.error("예외 발생: " + ex.getMessage());
            throw new CafeServerException("서버 오류 발생", ex);
        }
    }

    @Override
    public String logout(HttpServletRequest request) {
        // Authorization 헤더에서 JWT 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);  // Bearer 을 제외한 JWT 토큰

            // JWT 토큰 유효성 검증
            if (jwtTokenUtil.validateToken(jwt)) {
                // 토큰을 블랙리스트에 추가
                jwtTokenUtil.blacklistToken(jwt);
            } else {
                logger.warn("JWT 토큰 형식이 아닙니다.");
                throw new NotMatchedJWTPatternException("JWT 토큰이 만료되었습니다.", authorizationHeader);
            }
        } else {
            throw new CafeServerException("토큰이 없습니다. 로그인 후 재시도 해주세요", authorizationHeader);
        }
        return "정상적으로 로그아웃했습니다.";

    }
}
