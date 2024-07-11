package com.nhnacademy.codequestweb.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class JwtUtil {
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            // 토큰 파싱 중 다른 예외가 발생한 경우
            return false;
        }
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .unsecured()
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
