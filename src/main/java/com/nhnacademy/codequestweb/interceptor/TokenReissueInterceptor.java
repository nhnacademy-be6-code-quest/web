package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.response.auth.ClientLoginResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class TokenReissueInterceptor implements HandlerInterceptor {
    private final AuthClient authClient;

    public TokenReissueInterceptor(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws IOException {
        if (ex instanceof FeignException && ((FeignException) ex).status() == 303 && CookieUtils.getCookieValue(request, "refresh") != null) {

            try {
                ResponseEntity<ClientLoginResponseDto> reissueResponse = authClient.reissue(CookieUtils.getCookieValue(request, "refresh"));
                if (reissueResponse.getStatusCode().is2xxSuccessful() && reissueResponse.getBody() != null) {
                    Cookie accessCookie = new Cookie("access", reissueResponse.getHeaders().getValuesAsList("access").get(0));
                    accessCookie.setHttpOnly(true);
                    accessCookie.setSecure(true);
                    accessCookie.setPath("/");
                    accessCookie.setMaxAge(60 * 60 * 2);
                    response.addCookie(accessCookie);

                    Cookie refreshCookie = new Cookie("refresh", reissueResponse.getHeaders().getValuesAsList("refresh").get(0));
                    refreshCookie.setHttpOnly(true);
                    refreshCookie.setSecure(true);
                    refreshCookie.setPath("/");
                    refreshCookie.setMaxAge(60 * 60 * 24 * 14);
                    response.addCookie(refreshCookie);

                    response.sendRedirect(request.getRequestURI());
                } else {
                    response.sendRedirect("/auth");
                }
            } catch (Exception e) {
                response.sendRedirect("/auth");
            }
        }
    }
}