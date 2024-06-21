package com.nhnacademy.codequestweb.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class CookieUtils {
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
}

