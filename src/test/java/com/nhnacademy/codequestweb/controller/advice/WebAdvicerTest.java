package com.nhnacademy.codequestweb.controller.advice;

import feign.FeignException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class WebAdvicerTest {

    @InjectMocks
    private WebAdvicer webAdvicer;

    @Mock
    private FeignException.Unauthorized mockException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleBadRequest() {
        // 준비
        MockHttpServletResponse response = new MockHttpServletResponse();

        // 실행
        String result = webAdvicer.handleBadRequest(mockException, response);

        // 검증
        assertEquals("redirect:/auth", result);

        Cookie[] cookies = response.getCookies();
        assertEquals(2, cookies.length);

        for (Cookie cookie : cookies) {
            if ("access".equals(cookie.getName()) || "refresh".equals(cookie.getName())) {
                assertNull(cookie.getValue());
                assertEquals(0, cookie.getMaxAge());
                assertEquals("/", cookie.getPath());
            } else {
                fail("Unexpected cookie: " + cookie.getName());
            }
        }
    }

    @Test
    void testHandleBadRequestCookieDetails() {
        // 준비
        MockHttpServletResponse response = new MockHttpServletResponse();

        // 실행
        webAdvicer.handleBadRequest(mockException, response);

        // 검증
        Cookie[] cookies = response.getCookies();
        assertEquals(2, cookies.length);

        Cookie accessCookie = cookies[0];
        Cookie refreshCookie = cookies[1];

        assertEquals("access", accessCookie.getName());
        assertNull(accessCookie.getValue());
        assertEquals(0, accessCookie.getMaxAge());
        assertEquals("/", accessCookie.getPath());

        assertEquals("refresh", refreshCookie.getName());
        assertNull(refreshCookie.getValue());
        assertEquals(0, refreshCookie.getMaxAge());
        assertEquals("/", refreshCookie.getPath());
    }
}