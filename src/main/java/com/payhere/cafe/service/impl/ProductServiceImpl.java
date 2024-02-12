package com.payhere.cafe.service.impl;

import com.payhere.cafe.dto.ProductDTO;
import com.payhere.cafe.exception.CafeServerException;
import com.payhere.cafe.exception.DBConnectionException;
import com.payhere.cafe.exception.NotMatchedJWTPatternException;
import com.payhere.cafe.exception.NotMatchedProductException;
import com.payhere.cafe.model.Category;
import com.payhere.cafe.model.Product;
import com.payhere.cafe.modelmapper.ProductModelMapper;
import com.payhere.cafe.projection.UserIdProjection;
import com.payhere.cafe.repository.CategoryRepository;
import com.payhere.cafe.repository.ProductRepository;
import com.payhere.cafe.repository.UserRepository;
import com.payhere.cafe.service.ProductService;
import com.payhere.cafe.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductModelMapper productMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final Logger logger = LogManager.getLogger(this.getClass());
    private static final int PAGE_SIZE = 10;
    private static final String[] CHO = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};

    @Override
    @Transactional
    public ProductDTO registerProduct(ProductDTO productDTO, HttpServletRequest request) {
        try {
            String phoneNumber = this.jwtToUserPhoneNumber(request);
            //등록이 안된 카테고리 자동 추가
            if (!categoryRepository.existsByName(productDTO.getCategory())) {
                categoryRepository.save(Category.builder().name(productDTO.getCategory()).build());
            }

            Product product = productMapper.convertToEntity(productDTO);
            product.setChoseongName(this.convertToInitialSound(product.getName()));
            UserIdProjection userIdProjection = userRepository.findUserIdProjectionByPhoneNumber(phoneNumber);
            product.setUserId(userIdProjection.getId());
            Product resultProduct = productRepository.save(product);

            ProductDTO resultProductDTO = productMapper.convertToDTO(resultProduct);
            logger.info("상품 " + resultProduct.getName() + "을 등록에 성공했습니다.");
            return resultProductDTO;
        } catch (DataAccessException ex) {
            logger.error("데이터베이스 연결 예외 발생: " + ex.getMessage());
            throw new DBConnectionException("데이터베이스 연결 예외 발생", ex);
        }
    }

    private static String convertToInitialSound(String name) {
        StringBuilder result = new StringBuilder();

        for (char character : name.toCharArray()) {
            if (Character.getType(character) == Character.OTHER_LETTER) {
                char initialSound = (char) ((character - 0xAC00) / 28 / 21);
                result.append(CHO[(int) initialSound]);
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public String jwtToUserPhoneNumber(HttpServletRequest request) {
        String authorizationHeader = null;
        try {
            // Authorization 헤더에서 JWT 토큰 추출
            authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);  // Bearer 을 제외한 JWT 토큰

                // JWT 토큰 유효성 검증
                if (jwtTokenUtil.validateToken(jwt)) {
                    // 토큰에서 사용자 phone number 추출
                    return jwtTokenUtil.extractPhoneNumberFromToken(jwt);
                } else {
                    throw new NotMatchedJWTPatternException("JWT 토큰이 만료되었습니다.", authorizationHeader);
                }
            } else {
                logger.warn("JWT 토큰 형식이 아닙니다.");
                throw new NotMatchedJWTPatternException("JWT 토큰 형식이 아닙니다.", authorizationHeader);
            }
        } catch (NullPointerException ex) {
            throw new CafeServerException("토큰이 없습니다. 로그인 후 재시도 해주세요", authorizationHeader);
        }
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO, HttpServletRequest request) {
        try {
            this.jwtToUserPhoneNumber(request);
            if (!categoryRepository.existsByName(productDTO.getCategory())) {
                categoryRepository.save(Category.builder().name(productDTO.getCategory()).build());
            }
            Product findProduct = productRepository.findByProductId(productId);
            Product product = productMapper.convertToEntity(productDTO);

            if (findProduct != null) {
                product.setProductId(findProduct.getProductId());
                product.setUserId(findProduct.getUserId());
                Product resultProduct = productRepository.save(product);
                ProductDTO resultProductDTO = productMapper.convertToDTO(resultProduct);
                logger.info("상품 " + resultProduct.getName() + "을 수정에 성공했습니다.");
                return resultProductDTO;
            } else {
                throw new NotMatchedProductException("해당하는 상품을 찾지 못했습니다.", productId);
            }
        } catch (DataAccessException ex) {
            logger.error("데이터베이스 연결 예외 발생: " + ex.getMessage());
            throw new DBConnectionException("데이터베이스 연결 예외 발생", ex);
        }
    }

    @Override
    @Transactional
    public String deleteProduct(Long productId, HttpServletRequest request) {
        try {
            // Authorization 헤더에서 JWT 토큰 추출
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);  // Bearer 을 제외한 JWT 토큰

                // JWT 토큰 유효성 검증
                if (jwtTokenUtil.validateToken(jwt)) {
                    productRepository.deleteByProductId(productId);
                } else {
                    logger.warn("JWT 토큰 형식이 아닙니다.");
                    throw new NotMatchedJWTPatternException("JWT 토큰이 만료되었습니다.", authorizationHeader);
                }
            } else {
                throw new CafeServerException("토큰이 없습니다. 로그인 후 재시도 해주세요", authorizationHeader);
            }
            return "성공적으로 상품을 삭제했습니다.";
        } catch (DataAccessException ex) {
            logger.error("데이터베이스 연결 예외 발생: " + ex.getMessage());
            throw new DBConnectionException("데이터베이스 연결 예외 발생", ex);
        }
    }

    @Override
    public List<ProductDTO> selectProduct(int page, HttpServletRequest request) {
        List<ProductDTO> productDTOList = new ArrayList<>();
        try {
            String phoneNumber = this.jwtToUserPhoneNumber(request);
            UserIdProjection userIdProjection = userRepository.findUserIdProjectionByPhoneNumber(phoneNumber);

            Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
            Page<Product> productPage = productRepository.findByUserId(userIdProjection.getId(), pageable);
            List<Product> productList = productPage.getContent();
            for (Product resultProduct : productList) {
                ProductDTO resultProductDTO = productMapper.convertToDTO(resultProduct);
                productDTOList.add(resultProductDTO);
            }
            return productDTOList;
        } catch (DataAccessException ex) {
            logger.error("데이터베이스 연결 예외 발생: " + ex.getMessage());
            throw new DBConnectionException("데이터베이스 연결 예외 발생", ex);
        }
    }

    @Override
    public List<ProductDTO> searchProduct(String name, HttpServletRequest request) {
        List<ProductDTO> productDTOList = new ArrayList<>();
        try {
            this.jwtToUserPhoneNumber(request);
            if (!isConsonant(name)) {
                List<Product> productList = productRepository.findByNameLike(name);
                for (Product resultProduct : productList) {
                    ProductDTO resultProductDTO = productMapper.convertToDTO(resultProduct);
                    productDTOList.add(resultProductDTO);
                }
                return productDTOList;
            } else {
                List<Product> productList = productRepository.findByChoseongNameLike(name);
                for (Product resultProduct : productList) {
                    ProductDTO resultProductDTO = productMapper.convertToDTO(resultProduct);
                    productDTOList.add(resultProductDTO);
                }
                return productDTOList;
            }
        } catch (DataAccessException ex) {
            logger.error("데이터베이스 연결 예외 발생: " + ex.getMessage());
            throw new DBConnectionException("데이터베이스 연결 예외 발생", ex);
        }
    }

    private static boolean isConsonant(String name) {
        char[] arr = name.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            Boolean exist = false;
            for (int j = 0; j < CHO.length; j++) {
                if (CHO[j].charAt(0) == arr[i])
                    exist = true;
            }
            if (exist == false)
                return false;
        }
        return true;
    }

}
