package com.edubill.edubillApi.jwt;

import com.edubill.edubillApi.domain.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    private static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 1000 * 60 * 30L; // 30 분


    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key; //HMAC-SHA 키를 생성
    private final UserDetailsService userDetailsService;



    //  HMAC-SHA 키를 생성하는 데 사용되는 Base64 인코딩된 문자열을 다시 디코딩하여 키를 초기화하는 용도로 사용
    @PostConstruct
    public void init() {
        byte[] byteSecretKey = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(byteSecretKey);//디코팅된 바이트 배열을 기반으로 HMAC-SHA 알고르즘을 사용해서 Key객체로 반환 후 key 변수에 대입
    }

    // 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public JwtToken createTokenByLogin(String phoneNumber, UserRole role) {
        String accessToken = createToken(phoneNumber, role, ACCESS_TOKEN_TIME);
        return new JwtToken(accessToken);
    }

    private String createToken(String phoneNumber, UserRole role, Long tokenExpireTime) {

        return BEARER_PREFIX + Jwts.builder()
                .claim(AUTHORIZATION_KEY, role)
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            // 주어진 토큰을 파싱하기 위해 JWT 파서를 설정하고, 서명 키를 설정한 뒤, 토큰을 파싱하여 JWT 서명 검사를 수행합니다.
            Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;

        // 상위 클래스인 SecurityException을 연결하는 io.jsonwebtoken.SignatureException 는 deprecated 되어 사용 불가
        } catch (SignatureException e) {
            log.error("Invalid JWT signature, signature 가 유효하지 않은 토큰 입니다.");
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.error("Invalid JWT token, 유효하지 않은 jwt 토큰 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token. 만료된 jwt 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // role 제거 후 테스트 필요
    public Authentication createUserAuthentication(String phoneNumber) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}