//package com.edubill.edubillApi.jwt;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//@Component
//@Slf4j
//public class JwtTokenProvider {
//
//    private final Key key;
//
//    @Value("${jwt.accessTokenPeriod}")
//    private Long accessTokenPeriod;
//
//    @Value("${jwt.refreshTokenPeriod}")
//    private Long refreshTokenPeriod;
//
//    // application.properties에서 secret 값 가져와서 key에 저장
//    // 1. 주어진 시크릿 키는 Base64로 인코딩된 문자열.
//    //    문자열을 사용하는 이유는 데이터 전송할때 바이트 사용시의 문제를 해결하기 위함
//    //    이 문자열을 바이트코드로 변환해야 서버에서 사용가능
//    // 2. hmacShaKeyFor : HMAC-SHA 알고리즘에 사용할 키로 변환하는 역할. 이렇게 생성된 키는 서버에서 JWT 토큰을 검증할 때 사용
//
//    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey) {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    // User 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
//    public JwtToken generateToken(Authentication authentication) {
//        // 권한 가져오기
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//
//        Date now = new Date();
//        Date accessTokenExpiresDate = new Date(now.getTime() + accessTokenPeriod);
//        Date refreshTokenExpireDate = new Date(now.getTime() + refreshTokenPeriod);
//
//        // Access Token 생성
//        String accessToken = Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim("auth", authorities)
//                .setExpiration(accessTokenExpiresDate)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        // Refresh Token 생성
//        String refreshToken = Jwts.builder()
//                .setExpiration(refreshTokenExpireDate)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        return JwtToken.builder()
//                .grantType("Bearer")
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
//    // Jwt 토큰에서 추출한 정보를 사용하여 Spring Security의 Authentication 객체를 생성하여 반환하는 역할
//    public Authentication getAuthentication(String accessToken) {
//        // Jwt 토큰 복호화
//        Claims claims = parseClaims(accessToken);
//
//        if (claims.get("auth") == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        // 클레임에서 권한 정보 가져오기
//        // 권한 정보를 다양한 타입의 객체로 처리할 수 있고, 더 큰 유연성과 확장성을 가질 수 있음
//        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//
//        // User 객체를 만들어서 Authentication return
//        // UserDetails: interface, User: UserDetails를 구현한 class
//        log.info("claims subject := [{}]", claims.getSubject());
//        User principal = new User(claims.getSubject(), "", authorities);
//
//        UsernamePasswordAuthenticationToken test =
//                new UsernamePasswordAuthenticationToken(principal, "", authorities);
//
//        return test;
//    }
//
//    // 토큰 정보를 검증하는 메서드
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT Token", e);
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT Token", e);
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT Token", e);
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty.", e);
//        }
//        return false;
//    }
//
//
//    // accessToken
//    private Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(accessToken)
//                    .getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }
//}
