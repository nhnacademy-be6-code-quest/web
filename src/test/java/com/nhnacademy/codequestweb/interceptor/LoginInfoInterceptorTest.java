package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoginInfoInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private LoginInfoInterceptor loginInfoInterceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginInfoInterceptor = new LoginInfoInterceptor();
    }

    @Test
    void testPreHandle_Login() throws Exception {
        Cookie[] cookies = { new Cookie("access", "someAccessToken") };
        when(request.getCookies()).thenReturn(cookies);

        boolean result = loginInfoInterceptor.preHandle(request, response, new Object());

        verify(request).setAttribute("isLogin", "true");
        assertTrue(result);
    }

    @Test
    void testPreHandle_NotLogin() throws Exception {
        boolean result = loginInfoInterceptor.preHandle(request, response, new Object());

        verify(request, never()).setAttribute("isLogin", "true");
        assertTrue(result);
    }
}

