package com.edubill.edubillApi.jwt;

import com.edubill.edubillApi.domain.UserRole;
import com.edubill.edubillApi.exception.ErrorCode;
import com.edubill.edubillApi.exception.RefreshTokenInvalidException;
import com.edubill.edubillApi.repository.UserRefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
    private static final long REFRESH_TOKEN_TIME = 1000 * 60 * 60 * 24 * 7L;// 7일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key; //HMAC-SHA 키를 생성
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final UserDetailsService userDetailsService;
    private final UserRefreshTokenRepository refreshTokenMap;


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

    private String createToken(String phoneNumber, UserRole role, Long tokenExpireTime) {

        return BEARER_PREFIX + Jwts.builder()
                .claim(AUTHORIZATION_KEY, role)// JWT에 사용자 역할 정보를 클레임(claim)으로 추가합니다.
                .setSubject(phoneNumber)//JWT의 주제(subject)를 사용자 이름으로 설정합니다.
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtToken createTokenByLogin(String phoneNumber, UserRole role) {
        String accessToken = createToken(phoneNumber, role, ACCESS_TOKEN_TIME);
        String refreshToken = createToken(phoneNumber, role, REFRESH_TOKEN_TIME);
        refreshTokenMap.setRefreshToken(phoneNumber, refreshToken, REFRESH_TOKEN_TIME);
        return new JwtToken(accessToken, refreshToken);
    }

    //AccessToken 재발행 + refreshToken 함께 발행
    public JwtToken reissueAccessToken(String phoneNumber, UserRole role, String reToken) {
        // 저장된 refresh token을 가져와서 입력된 reToken 같은지 유무 확인
        if (!refreshTokenMap.getRefreshToken(phoneNumber).equals(reToken)) {
            throw new RefreshTokenInvalidException("refresh token이 일치하지 않음");
        }
        String accessToken = createToken(phoneNumber, role, ACCESS_TOKEN_TIME);
        String refreshToken = createToken(phoneNumber, role, REFRESH_TOKEN_TIME);
        refreshTokenMap.setRefreshToken(phoneNumber, refreshToken, REFRESH_TOKEN_TIME);
        return new JwtToken(accessToken, refreshToken);
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
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
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            log.info("Invalid JWT token, 만료된 jwt 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Long getExpiration(String accessToken) {
        //access token 만료시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        //현재시간
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public Authentication createUserAuthentication(String phoneNumber) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}