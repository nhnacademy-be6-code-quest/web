package com.nhnacademy.codequestweb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpHeaders;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CookieUtils {
    private static final String AUTHORIZATION = "access";

    private CookieUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> name.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public static boolean isGuest(HttpServletRequest req) {
        String accessToken = CookieUtils.getCookieValue(req, AUTHORIZATION);
        String refreshToken = CookieUtils.getCookieValue(req, "refresh");
        return accessToken == null && refreshToken == null;
    }

    public static HttpHeaders setHeader(HttpServletRequest req){
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, CookieUtils.getCookieValue(req, AUTHORIZATION));
        return headers;
    }

    public static void setCookieValue(HttpServletResponse resp, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(7200);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        resp.addCookie(cookie);
    }

    public static void setCartCookieValue(List<CartRequestDto> cartListOfCookie, ObjectMapper objectMapper, HttpServletResponse resp) throws JsonProcessingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String updatedCartJson = objectMapper.writeValueAsString(cartListOfCookie);
        String encryptedUpdatedCartJson = SecretKeyUtils.encrypt(updatedCartJson, SecretKeyUtils.getSecretKey());
        CookieUtils.setCookieValue(resp,"cart", encryptedUpdatedCartJson);
    }


    public static void deleteCookieValue(HttpServletResponse resp, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        resp.addCookie(cookie);
    }
}

