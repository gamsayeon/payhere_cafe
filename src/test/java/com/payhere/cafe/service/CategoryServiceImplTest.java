package com.payhere.cafe.service;

import com.payhere.cafe.repository.CategoryRepository;
import com.payhere.cafe.util.JwtTokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@DisplayName("CategoryServiceImpl Unit 테스트")
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    private String TEST_CATEGORY_NAME = "testCategoryName";

    @Test
    @DisplayName("카테고리 삭제 성공 테스트")
    void deleteCategory() {
        //given
        String jwtToken = "유효한_jwt_토큰";
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer " + jwtToken);
        when(jwtTokenUtil.validateToken(jwtToken)).thenReturn(true);

        //when, then
        assertDoesNotThrow(() -> categoryService.deleteCategory(TEST_CATEGORY_NAME, mockRequest));
    }
}