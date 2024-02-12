package com.payhere.cafe.service;

import com.payhere.cafe.dto.UserDTO;
import com.payhere.cafe.model.User;
import com.payhere.cafe.modelmapper.UserModelMapper;
import com.payhere.cafe.repository.UserRepository;
import com.payhere.cafe.util.JwtTokenUtil;
import com.payhere.cafe.util.Sha256Encrypt;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("UserServiceImpl Unit 테스트")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserModelMapper userMapper;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    private UserDTO userDTO;
    private User user;
    private String TEST_PASSWORD = "testPassword";
    private String TEST_ENCRYPT_PASSWORD = Sha256Encrypt.encrypt(TEST_PASSWORD);
    private String TEST_JWT_TOKEN = "유효한 JWT Token";

    @BeforeEach
    public void generateTestUser() {
        userDTO = UserDTO.builder()
                .phoneNumber("010-1234-5678")
                .password(TEST_PASSWORD)
                .build();

        user = User.builder()
                .phoneNumber("010-1234-5678")
                .password(TEST_ENCRYPT_PASSWORD)
                .build();
    }

    @Test
    void signUp() {
        //given
        when(userMapper.convertToEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.convertToDTO(user)).thenReturn(UserDTO.builder().password(TEST_ENCRYPT_PASSWORD).build());

        //when
        UserDTO result = userService.signUp(userDTO);

        //then
        assertNotNull(result);
        assertEquals(TEST_ENCRYPT_PASSWORD, result.getPassword());
    }

    @Test
    void login() {
        //given
        when(userMapper.convertToEntity(userDTO)).thenReturn(user);
        when(userRepository.findByPhoneNumberAndPassword(user.getPhoneNumber(), user.getPassword()))
                .thenReturn(user);
        when(jwtTokenUtil.generateToken(user.getPhoneNumber())).thenReturn(TEST_JWT_TOKEN);

        //when
        String result = userService.login(userDTO);

        //then
        assertNotNull(result);
        assertEquals(TEST_JWT_TOKEN, result);
    }

    @Test
    void logout() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");
        when(jwtTokenUtil.validateToken("validJwtToken")).thenReturn(true);

        //when, then
        assertDoesNotThrow(() -> userService.logout(request));
    }
}