package com.nhnacademy.codequestweb.controller.auth;

import com.nhnacademy.codequestweb.request.auth.ClientLoginRequestDto;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.auth.OAuthRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import com.nhnacademy.codequestweb.service.auth.AuthService;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testAuth() {
        String result = authController.auth(request);
        assertEquals("index", result);
        assertEquals("auth", request.getAttribute("view"));
    }

    @Test
    void testAuthWithAccessToken() {
        request.setCookies(new Cookie("access", "access_token"));
        String result = authController.auth(request);
        assertEquals("redirect:/", result);
    }

    @Test
    void testAuthPost() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        String result = authController.authPost(dto, request);
        assertEquals("redirect:/auth?alterMessage=Success", result);
        verify(authService).register(dto);
    }

    @Test
    void testLoginPost() {
        ClientLoginRequestDto dto = new ClientLoginRequestDto();
        TokenResponseDto tokenResponse = new TokenResponseDto("access_token", "refresh_token");

        when(authService.login(dto)).thenReturn(ResponseEntity.ok(tokenResponse));

        String result = authController.loginPost(dto, response);

        assertEquals("redirect:/", result);
        Cookie accessCookie = response.getCookie("access");
        Cookie refreshCookie = response.getCookie("refresh");
        assertNotNull(accessCookie);
        assertNotNull(refreshCookie);
        assertEquals("access_token", accessCookie.getValue());
        assertEquals("refresh_token", refreshCookie.getValue());
    }

    @Test
    void testLogout() {
        request.setCookies(new Cookie("access", "access_token"), new Cookie("refresh", "refresh_token"));

        String result = authController.logout(request, response);

        assertEquals("redirect:/", result);
        Cookie accessCookie = response.getCookie("access");
        Cookie refreshCookie = response.getCookie("refresh");
        assertNotNull(accessCookie);
        assertNotNull(refreshCookie);
        assertEquals(0, accessCookie.getMaxAge());
        assertEquals(0, refreshCookie.getMaxAge());
    }

    @Test
    void testPaycoLogin() {
        when(authService.getPaycoLoginURL()).thenReturn("http://payco.login.url");
        authController.paycoLogin(request, response);
        assertEquals("http://payco.login.url", response.getRedirectedUrl());
    }

    @Test
    void testPaycoRecovery() {
        when(authService.getPaycoRecoveryURL()).thenReturn("http://payco.recovery.url");
        authController.paycoRecovery(request, response);
        assertEquals("http://payco.recovery.url", response.getRedirectedUrl());
    }

    @Test
    void testPaycoLoginCallback() {
        TokenResponseDto tokenResponse = new TokenResponseDto("access_token", "refresh_token");
        when(authService.paycoLoginCallback("test_code")).thenReturn(ResponseEntity.ok(tokenResponse));

        String result = authController.paycoLoginCallback("test_code", null, request, response);

        assertEquals("redirect:/", result);
        verify(authService).paycoLoginCallback("test_code");
    }

    @Test
    void testPaycoLoginCallbackWithError() {
        String result = authController.paycoLoginCallback(null, "error", request, response);
        assertEquals("redirect:/", result);
    }

    @Test
    void testPaycoRecoveryCallback() {
        when(authService.recover("test_code")).thenReturn(ResponseEntity.ok("test@example.com"));
        String result = authController.paycoRecoveryCallback("test_code", null, request, response);
        assertEquals("redirect:/auth?alterMessage=Success", result);
        verify(authService).recover("test_code");
        verify(authService).recoverAccount("test@example.com");
    }

    @Test
    void testOauthRegister() {
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto();
        TokenResponseDto tokenResponse = new TokenResponseDto("access_token", "refresh_token");

        when(authService.oAuthRegister(dto)).thenReturn(ResponseEntity.ok(tokenResponse));

        String result = authController.oauthRegister(dto, request, response);

        assertEquals("redirect:/", result);
        Cookie accessCookie = response.getCookie("access");
        Cookie refreshCookie = response.getCookie("refresh");
        assertNotNull(accessCookie);
        assertNotNull(refreshCookie);
        assertEquals("access_token", accessCookie.getValue());
        assertEquals("refresh_token", refreshCookie.getValue());
    }

    @Test
    void testLoginError() {
        FeignException.Unauthorized exception = mock(FeignException.Unauthorized.class);
        String result = authController.loginError(exception, request, response);
        assertEquals("index", result);
        assertEquals("아이디 또는 비밀번호가 틀렸습니다.", request.getAttribute("login_message"));
        assertEquals("auth", request.getAttribute("view"));
    }

    @Test
    void testLoginErrorGone() {
        FeignException.Gone exception = mock(FeignException.Gone.class);
        String result = authController.loginError(exception, request, response);
        assertEquals("index", result);
        assertEquals("삭제/휴면된 계정입니다.", request.getAttribute("login_message"));
        assertEquals("auth", request.getAttribute("view"));
        assertTrue((Boolean) request.getAttribute("isDeleted"));
    }

    @Test
    void testValidationErrorConflict() {
        FeignException.Conflict exception = mock(FeignException.Conflict.class);
        request.setParameter("clientName", "Test");
        request.setParameter("clientEmail", "test@example.com");
        request.setParameter("clientPassword", "password");
        request.setParameter("clientPhoneNumber", "1234567890");
        request.setParameter("clientBirth", "1990-01-01");

        String result = authController.validationError(exception, request);

        assertEquals("index", result);
        assertNotNull(request.getAttribute("prev_data"));
        assertEquals("이미 가입된 이메일입니다.", request.getAttribute("register_message"));
        assertEquals("auth", request.getAttribute("view"));
        assertEquals("register", request.getAttribute("form"));
    }
}
