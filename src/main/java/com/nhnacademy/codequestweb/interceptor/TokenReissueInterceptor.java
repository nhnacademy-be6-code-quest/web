package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenReissueInterceptor implements HandlerInterceptor {
    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";
    private final AuthClient authClient;

    public TokenReissueInterceptor(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String access = CookieUtils.getCookieValue(request, ACCESS);
        if (access != null && JwtUtil.isTokenExpired(access)) {
            log.info("is expired (pre)");
            HttpHeaders headers = new HttpHeaders();
            headers.set(ACCESS, CookieUtils.getCookieValue(request, ACCESS));
            headers.set(REFRESH, CookieUtils.getCookieValue(request, REFRESH));
            ResponseEntity<TokenResponseDto> reissueResponse = authClient.reissue(headers);
            if (reissueResponse.getStatusCode().is2xxSuccessful() && reissueResponse.getBody() != null) {
                String newAccess = reissueResponse.getBody().getAccess();
                String newRefresh = reissueResponse.getBody().getRefresh();

                // 새 액세스 토큰으로 쿠키 업데이트
                Cookie accessCookie = new Cookie(ACCESS, newAccess);
                accessCookie.setHttpOnly(true);
                accessCookie.setSecure(true);
                accessCookie.setPath("/");
                accessCookie.setMaxAge(60 * 60 * 24 * 14);
                response.addCookie(accessCookie);

                // 새 리프레시 토큰으로 쿠키 업데이트
                Cookie refreshCookie = new Cookie(REFRESH, newRefresh);
                refreshCookie.setHttpOnly(true);
                refreshCookie.setSecure(true);
                refreshCookie.setPath("/");
                refreshCookie.setMaxAge(60 * 60 * 24 * 14);
                response.addCookie(refreshCookie);

                log.info("Token reissued success(pre)");

                // 요청 객체의 쿠키도 업데이트
                updateRequestCookies(request, newAccess, newRefresh);
            } else {
                log.info("refresh token expired(pre)");
                removeCookie(response);
            }
        }
        return true;
    }

    private void updateRequestCookies(HttpServletRequest request, String newAccess, String newRefresh) {
        // 현재 요청의 쿠키를 가져옵니다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(ACCESS)) {
                    cookie.setValue(newAccess);
                } else if (cookie.getName().equals(REFRESH)) {
                    cookie.setValue(newRefresh);
                }
            }
        }
    }

    private void removeCookie(HttpServletResponse response) {
        Cookie access = new Cookie(ACCESS, null);
        Cookie refresh = new Cookie(REFRESH, null);
        access.setMaxAge(0);
        refresh.setMaxAge(0);
        access.setPath("/");
        refresh.setPath("/");
        response.addCookie(access);
        response.addCookie(refresh);
        CookieUtils.deleteCookieValue(response, "cart");
    }

}