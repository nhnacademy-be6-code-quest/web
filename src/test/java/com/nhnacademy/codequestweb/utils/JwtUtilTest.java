package com.nhnacademy.codequestweb.utils;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String VALID_TOKEN = createValidToken();
    private static final String EXPIRED_TOKEN = createExpiredToken();
    private static final String MALFORMED_TOKEN = "bWFsZm9ybWVk.VE9LRU4=.VEVTVA0K";

    private static String createValidToken() {
        long now = System.currentTimeMillis() / 1000;
        String payload = "{\"exp\":" + (now + 60) + "}";
        return createTokenFromPayload(payload);
    }

    private static String createExpiredToken() {
        long now = System.currentTimeMillis() / 1000;
        String payload = "{\"exp\":" + (now - 60) + "}";
        return createTokenFromPayload(payload);
    }

    private static String createTokenFromPayload(String payload) {
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        return Base64.getUrlEncoder().encodeToString(header.getBytes()) + "." +
                Base64.getUrlEncoder().encodeToString(payload.getBytes()) + ".signature";
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        assertFalse(JwtUtil.isTokenExpired(VALID_TOKEN));
    }

    @Test
    void testIsTokenExpired_ExpiredToken() {
        assertTrue(JwtUtil.isTokenExpired(EXPIRED_TOKEN));
    }
}
