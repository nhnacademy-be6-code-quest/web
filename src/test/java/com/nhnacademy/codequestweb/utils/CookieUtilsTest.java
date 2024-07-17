package com.nhnacademy.codequestweb.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CookieUtilsTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCookieValue() {
        Cookie cookie = new Cookie("test", "value");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String value = CookieUtils.getCookieValue(request, "test");
        assertEquals("value", value);
    }

    @Test
    void testGetCookieValue_NoCookies() {
        when(request.getCookies()).thenReturn(null);

        String value = CookieUtils.getCookieValue(request, "test");
        assertNull(value);
    }

    @Test
    void testIsGuest() {
        when(request.getCookies()).thenReturn(null);
        assertTrue(CookieUtils.isGuest(request));
    }

    @Test
    void testSetHeader() {
        Cookie cookie = new Cookie("access", "tokenValue");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        HttpHeaders headers = CookieUtils.setHeader(request);
        assertEquals("tokenValue", headers.getFirst("access"));
    }

    @Test
    void testSetCookieValue() {
        CookieUtils.setCookieValue(response, "test", "value");

        verify(response, times(1)).addCookie(argThat(cookie ->
                "test".equals(cookie.getName()) &&
                        "value".equals(cookie.getValue()) &&
                        cookie.getMaxAge() == 7200 &&
                        "/".equals(cookie.getPath()) &&
                        cookie.isHttpOnly() &&
                        cookie.getSecure()
        ));
    }

    @Test
    void testDeleteCookieValue() {
        CookieUtils.deleteCookieValue(response, "test");

        verify(response, times(1)).addCookie(argThat(cookie ->
                "test".equals(cookie.getName()) &&
                        cookie.getValue() == null &&
                        cookie.getMaxAge() == 0 &&
                        "/".equals(cookie.getPath()) &&
                        cookie.isHttpOnly() &&
                        cookie.getSecure()
        ));
    }
}
