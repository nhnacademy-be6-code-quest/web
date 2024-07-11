package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.JwtUtil;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@Slf4j
@Component
public class TokenReissueInterceptor implements HandlerInterceptor {
    private final AuthClient authClient;

    public TokenReissueInterceptor(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String access = CookieUtils.getCookieValue(request, "access");
        if (access != null && JwtUtil.isTokenExpired(access)) {
            log.info("is expired (pre)");
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(request, "access"));
            headers.set("refresh", CookieUtils.getCookieValue(request, "refresh"));
            ResponseEntity<TokenResponseDto> reissueResponse = authClient.reissue(headers);
            if (reissueResponse.getStatusCode().is2xxSuccessful() && reissueResponse.getBody() != null) {
                String newAccess = reissueResponse.getBody().getAccess();
                String newRefresh = reissueResponse.getBody().getRefresh();

                // 새 액세스 토큰으로 쿠키 업데이트
                Cookie accessCookie = new Cookie("access", newAccess);
                accessCookie.setHttpOnly(true);
                accessCookie.setSecure(true);
                accessCookie.setPath("/");
                accessCookie.setMaxAge(60 * 60 * 24 * 14);
                response.addCookie(accessCookie);

                // 새 리프레시 토큰으로 쿠키 업데이트
                Cookie refreshCookie = new Cookie("refresh", newRefresh);
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
                if (cookie.getName().equals("access")) {
                    cookie.setValue(newAccess);
                } else if (cookie.getName().equals("refresh")) {
                    cookie.setValue(newRefresh);
                }
            }
        }

        // 요청 객체의 쿠키를 직접 수정할 수 없으므로, 래퍼 클래스를 사용합니다.
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public Cookie[] getCookies() {
                return cookies;
            }
        };

        // 원래의 request를 wrappedRequest로 교체합니다.
        // 이 부분은 프레임워크나 서버 설정에 따라 다를 수 있습니다.
        // 예를 들어, Spring의 RequestContextHolder를 사용할 수 있습니다.
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(wrappedRequest));
    }

    private void removeCookie(HttpServletResponse response) {
        Cookie access = new Cookie("access", null);
        Cookie refresh = new Cookie("refresh", null);
        access.setMaxAge(0);
        refresh.setMaxAge(0);
        access.setPath("/");
        refresh.setPath("/");
        response.addCookie(access);
        response.addCookie(refresh);
        CookieUtils.deleteCookieValue(response, "cart");
    }

}