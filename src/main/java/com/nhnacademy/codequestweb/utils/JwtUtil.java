package com.nhnacademy.codequestweb.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.Date;

@Slf4j
public class JwtUtil {
    public static boolean isTokenExpired(String token) {
        try {
//            log.info("token: {}", token);
            Date expirationDate = extractExpirationDate(token);
            Date now = new Date();
            long differenceInMillis = expirationDate.getTime() - now.getTime();

//            log.info("expiration date is {}, now {}", expirationDate, now);
            // 예를 들어, 5초의 여유를 두고 만료 여부를 판단
            return differenceInMillis < 5000;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            // 토큰 파싱 중 다른 예외가 발생한 경우
            log.error("Token parsing error", e);
            return false;
        }
    }

    private static Date extractExpirationDate(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new MalformedJwtException("JWT 문자열이 유효하지 않습니다.");
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        int expIndex = payload.indexOf("\"exp\":");
        if (expIndex == -1) {
            throw new MalformedJwtException("만료 시간이 없습니다.");
        }

        int colonIndex = payload.indexOf(':', expIndex);
        int commaIndex = payload.indexOf(',', colonIndex);
        if (commaIndex == -1) {
            commaIndex = payload.indexOf('}', colonIndex);
        }

        String expValue = payload.substring(colonIndex + 1, commaIndex).trim();
        long exp = Long.parseLong(expValue);
        return new Date(exp * 1000L);
    }
}
