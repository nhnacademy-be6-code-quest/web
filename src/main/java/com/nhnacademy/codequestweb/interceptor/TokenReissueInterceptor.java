package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

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
                HttpHeaders headers = new HttpHeaders();
                headers.set("access", CookieUtils.getCookieValue(request, "access"));
                headers.set("refresh", CookieUtils.getCookieValue(request, "refresh"));
                ResponseEntity<TokenResponseDto> reissueResponse = authClient.reissue(headers);
                if (reissueResponse.getStatusCode().is2xxSuccessful() && reissueResponse.getBody() != null) {
                    Cookie accessCookie = new Cookie("access", reissueResponse.getBody().getAccess());
                    accessCookie.setHttpOnly(true);
                    accessCookie.setSecure(true);
                    accessCookie.setPath("/");
                    accessCookie.setMaxAge(60 * 60 * 2);
                    response.addCookie(accessCookie);

                    Cookie refreshCookie = new Cookie("refresh", reissueResponse.getBody().getRefresh());
                    refreshCookie.setHttpOnly(true);
                    refreshCookie.setSecure(true);
                    refreshCookie.setPath("/");
                    refreshCookie.setMaxAge(60 * 60 * 24 * 14);
                    response.addCookie(refreshCookie);

                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<html><body>");
                    out.println("<script type=\"text/javascript\">");
                    out.println("window.location.reload(true);");
                    out.println("</script>");
                    out.println("</body></html>");
                    out.close();
                } else {
                    response.sendRedirect("/auth");
                }
            } catch (Exception e) {
                response.sendRedirect("/auth");
            }
        }
    }
}