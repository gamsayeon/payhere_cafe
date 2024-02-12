package com.payhere.cafe.service;

import com.payhere.cafe.dto.ProductDTO;
import com.payhere.cafe.model.Category;
import com.payhere.cafe.model.Product;
import com.payhere.cafe.modelmapper.ProductModelMapper;
import com.payhere.cafe.projection.UserIdProjection;
import com.payhere.cafe.repository.CategoryRepository;
import com.payhere.cafe.repository.ProductRepository;
import com.payhere.cafe.repository.UserRepository;
import com.payhere.cafe.service.impl.ProductServiceImpl;
import com.payhere.cafe.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("ProductServiceImpl Unit 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private ProductModelMapper productMapper;
    private Product product;
    private Category category;
    private ProductDTO productDTO;
    private String TEST_JWT_TOKEN = "유효한_jwt_토큰";
    private int TEST_PAGE = 1;
    private static final int PAGE_SIZE = 10;

    @BeforeEach
    public void generateTestProduct() {
        productDTO = ProductDTO.builder()
                .name("상품 이름")
                .category("TestCategory")
                .build();

        product = Product.builder()
                .productId(1L)
                .name("상품 이름")
                .category("TestCategory")
                .build();

        category = Category.builder().name("TestCategory").build();
    }

    @Test
    void registerProduct() {
        //given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer " + TEST_JWT_TOKEN);
        when(jwtTokenUtil.validateToken(TEST_JWT_TOKEN)).thenReturn(true);
        when(jwtTokenUtil.extractPhoneNumberFromToken(TEST_JWT_TOKEN)).thenReturn("010-1234-5678");

        when(categoryRepository.existsByName("TestCategory")).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(category);
        when(productMapper.convertToEntity(productDTO)).thenReturn(product);

        when(userRepository.findUserIdProjectionByPhoneNumber("010-1234-5678")).thenAnswer(invocation -> {
            UserIdProjection mockUserIdProjection = mock(UserIdProjection.class);
            when(mockUserIdProjection.getId()).thenReturn(123L); // 원하는 ID 값으로 대체
            return mockUserIdProjection;
        });

        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.convertToDTO(product)).thenReturn(productDTO);

        // when
        ProductDTO resultProductDTO = productService.registerProduct(productDTO, mockRequest);

        // then
        assertNotNull(resultProductDTO);
        assertEquals("상품 이름", resultProductDTO.getName());
        assertEquals("TestCategory", resultProductDTO.getCategory());
    }

    @Test
    void updateProduct() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");
        when(jwtTokenUtil.validateToken("validJwtToken")).thenReturn(true);
        when(jwtTokenUtil.extractPhoneNumberFromToken("validJwtToken")).thenReturn("010-1234-5678");

        when(categoryRepository.existsByName("TestCategory")).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(Category.builder().name("TestCategory").build());
        when(productMapper.convertToEntity(productDTO)).thenReturn(product);

        when(productRepository.findByProductId(product.getProductId())).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.convertToDTO(product)).thenReturn(productDTO);
        // when
        ProductDTO resultProductDTO = productService.updateProduct(product.getProductId(), productDTO, request);

        // then
        assertNotNull(resultProductDTO);
        assertEquals("상품 이름", resultProductDTO.getName());
        assertEquals("TestCategory", resultProductDTO.getCategory());
    }

    @Test
    void deleteProduct() {
        //given
        String jwtToken = "유효한_jwt_토큰";
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer " + jwtToken);
        when(jwtTokenUtil.validateToken(jwtToken)).thenReturn(true);

        //when, then
        assertDoesNotThrow(() -> productService.deleteProduct(product.getProductId(), mockRequest));
    }

    @Test
    void selectProduct() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");
        when(jwtTokenUtil.validateToken("validJwtToken")).thenReturn(true);
        when(jwtTokenUtil.extractPhoneNumberFromToken("validJwtToken")).thenReturn("010-1234-5678");

        when(userRepository.findUserIdProjectionByPhoneNumber("010-1234-5678")).thenAnswer(invocation -> {
            UserIdProjection mockUserIdProjection = mock(UserIdProjection.class);
            when(mockUserIdProjection.getId()).thenReturn(123L); // 원하는 ID 값으로 대체
            return mockUserIdProjection;
        });

        Pageable pageable = PageRequest.of(TEST_PAGE - 1, PAGE_SIZE);
        List<Product> productList = Collections.singletonList(product);

        when(productRepository.findByUserId(123L, pageable)).thenReturn(new PageImpl<>(productList, pageable, productList.size()));

        when(productMapper.convertToDTO(product)).thenReturn(productDTO);

        //when
        List<ProductDTO> productDTOList = productService.selectProduct(TEST_PAGE, request);

        //then
        for (ProductDTO resultProductDTO : productDTOList) {
            assertEquals(resultProductDTO.getName(), product.getName());
        }
    }

    @Test
    void searchProduct() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");
        when(jwtTokenUtil.validateToken("validJwtToken")).thenReturn(true);
        when(jwtTokenUtil.extractPhoneNumberFromToken("validJwtToken")).thenReturn("010-1234-5678");
        when(productRepository.findByNameLike(product.getName())).thenReturn(List.of(product));
        when(productMapper.convertToDTO(any(Product.class))).thenReturn(productDTO);

        //when
        List<ProductDTO> resultDTOList = productService.searchProduct(product.getName(), request);

        //then
        assertNotNull(resultDTOList);
        assertEquals(List.of(product).size(), resultDTOList.size());
    }
}