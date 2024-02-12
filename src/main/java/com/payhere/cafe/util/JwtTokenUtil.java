package com.payhere.cafe.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final Key secretKey;
    private final SignatureAlgorithm signatureAlgorithm;

    @Value("${jwt.expiration}")
    private long expiration; // 토큰 만료 시간

    private final Logger logger = LogManager.getLogger(this.getClass());
    private Map<String, String> latestTokens = new HashMap<>();
    private Set<String> tokenBlacklist = new HashSet<>(); // 블랙리스트

    public String generateToken(String phoneNumber) {
        // 사용자별로 최근에 발급된 토큰 가져오기
        String latestToken = latestTokens.get(phoneNumber);

        // 최근에 발급된 토큰이 만료되었거나 없으면 새로운 토큰 발급
        if (latestToken == null || !validateToken(latestToken)) {
            Date expirationDate = new Date(System.currentTimeMillis() + expiration);
            String newToken = Jwts.builder()
                    .setSubject(phoneNumber)
                    .setExpiration(expirationDate)
                    .signWith(secretKey, signatureAlgorithm)
                    .compact();

            // 최근에 발급된 토큰 저장
            latestTokens.put(phoneNumber, newToken);

            return newToken;
        }

        // 만료되지 않은 최근 토큰 반환
        return latestToken;
    }

    public String extractPhoneNumberFromToken(String token) {
        return extractClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token) {    //해당하는 토큰의 만료되지 않은지 체크 만료전: true , 만료후: false
        try {
            if (tokenBlacklist.contains(token)) {
                // 블랙리스트에 있는 토큰은 유효하지 않음
                return false;
            }
            return extractClaimsFromToken(token)
                    .getExpiration()
                    .after(new Date());
        } catch (ExpiredJwtException ex) {   //해당 토큰이 만료되게 되면 예외 발생 체크 용도로 사용하기에 false 반환
            tokenBlacklist.add(token);
            logger.warn("해당 토큰은 만료하였습니다.");
            return false;
        }
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 블랙리스트에 토큰 추가
    public void blacklistToken(String token) {
        tokenBlacklist.add(token);
    }
}
