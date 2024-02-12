package com.payhere.cafe.service;

import com.payhere.cafe.exception.CafeServerException;
import com.payhere.cafe.exception.NotMatchedJWTPatternException;
import com.payhere.cafe.repository.CategoryRepository;
import com.payhere.cafe.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    @Transactional
    public String deleteCategory(String name, HttpServletRequest request) {
        try {
            // Authorization 헤더에서 JWT 토큰 추출
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);  // Bearer 을 제외한 JWT 토큰

                // JWT 토큰 유효성 검증
                if (jwtTokenUtil.validateToken(jwt)) {
                    categoryRepository.deleteByName(name);
                } else {
                    logger.warn("JWT 토큰 형식이 아닙니다.");
                    throw new NotMatchedJWTPatternException("JWT 토큰이 만료되었습니다.", authorizationHeader);
                }
            } else {
                throw new CafeServerException("토큰이 없습니다. 로그인 후 재시도 해주세요", authorizationHeader);
            }
            return "성공적으로 카테고리를 삭제했습니다.";
        } catch (DataIntegrityViolationException ex) {
            //외래키 제약조건 위반 예외
            throw new CafeServerException("해당 카테고리의 상품이 있습니다.", ex);
        }
    }
}
