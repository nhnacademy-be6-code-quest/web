package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TokenReissueInterceptorTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private TokenReissueInterceptor tokenReissueInterceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void preHandle_TokenExpired_ReissueSuccess() throws Exception {
        String expiredAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyMzkwMjJ9.4Adcj3UFYzPUVaVF43FmMab6RlaQD8A9V8wFzzht-KQ";
        String refreshToken = "validRefreshToken";
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";

        Cookie accessCookie = new Cookie("access", expiredAccessToken);
        Cookie refreshCookie = new Cookie("refresh", refreshToken);
        Cookie[] cookies = {accessCookie, refreshCookie};

        when(request.getCookies()).thenReturn(cookies);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(newAccessToken, newRefreshToken);
        ResponseEntity<TokenResponseDto> responseEntity = ResponseEntity.ok(tokenResponseDto);

        when(authClient.reissue(any(HttpHeaders.class))).thenReturn(responseEntity);

        boolean result = tokenReissueInterceptor.preHandle(request, response, new Object());

        verify(authClient).reissue(any(HttpHeaders.class));
        verify(response, times(2)).addCookie(any(Cookie.class));

        assertTrue(result);
    }

    @Test
    void preHandle_TokenExpired_ReissueFail() throws Exception {
        String expiredAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyMzkwMjJ9.4Adcj3UFYzPUVaVF43FmMab6RlaQD8A9V8wFzzht-KQ";;
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyMzkwMjJ9.4Adcj3UFYzPUVaVF43FmMab6RlaQD8A9V8wFzzht-KQ";;

        Cookie accessCookie = new Cookie("access", expiredAccessToken);
        Cookie refreshCookie = new Cookie("refresh", refreshToken);
        Cookie[] cookies = {accessCookie, refreshCookie};

        when(request.getCookies()).thenReturn(cookies);

        ResponseEntity<TokenResponseDto> responseEntity = ResponseEntity.badRequest().body(null);

        when(authClient.reissue(any(HttpHeaders.class))).thenReturn(responseEntity);

        boolean result = tokenReissueInterceptor.preHandle(request, response, new Object());

        verify(authClient).reissue(any(HttpHeaders.class));
        verify(response, times(3)).addCookie(any(Cookie.class)); // two for access and refresh removal, one for cart deletion

        assertTrue(result);
    }

    @Test
    void preHandle_TokenNotExpired() throws Exception {
        String validAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxOTE2MjM5MDIyfQ.DGme0_qbXW79j7VftKA1L1b6qugxEJ6-nqcmnvmi7DA";

        Cookie accessCookie = new Cookie("access", validAccessToken);
        Cookie[] cookies = {accessCookie};

        when(request.getCookies()).thenReturn(cookies);

        boolean result = tokenReissueInterceptor.preHandle(request, response, new Object());

        verify(authClient, never()).reissue(any(HttpHeaders.class));
        verify(response, never()).addCookie(any(Cookie.class));

        assertTrue(result);
    }
}
