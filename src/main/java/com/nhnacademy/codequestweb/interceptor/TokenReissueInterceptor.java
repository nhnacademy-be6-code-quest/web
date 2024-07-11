package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.JwtUtil;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(request, "access"));
            headers.set("refresh", CookieUtils.getCookieValue(request, "refresh"));
            ResponseEntity<TokenResponseDto> reissueResponse = authClient.reissue(headers);
            if (reissueResponse.getStatusCode().is2xxSuccessful() && reissueResponse.getBody() != null) {
                Cookie accessCookie = new Cookie("access", reissueResponse.getBody().getAccess());
                accessCookie.setHttpOnly(true);
                accessCookie.setSecure(true);
                accessCookie.setPath("/");
                accessCookie.setMaxAge(60 * 60 * 24 * 14);
                response.addCookie(accessCookie);

                Cookie refreshCookie = new Cookie("refresh", reissueResponse.getBody().getRefresh());
                refreshCookie.setHttpOnly(true);
                refreshCookie.setSecure(true);
                refreshCookie.setPath("/");
                refreshCookie.setMaxAge(60 * 60 * 24 * 14);
                response.addCookie(refreshCookie);

                log.info("Token reissued success");
            } else {
                log.info("refresh token expired");
                removeCookie(response);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
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
                    accessCookie.setMaxAge(60 * 60 * 24 * 14);
                    response.addCookie(accessCookie);

                    Cookie refreshCookie = new Cookie("refresh", reissueResponse.getBody().getRefresh());
                    refreshCookie.setHttpOnly(true);
                    refreshCookie.setSecure(true);
                    refreshCookie.setPath("/");
                    refreshCookie.setMaxAge(60 * 60 * 24 * 14);
                    response.addCookie(refreshCookie);

                    log.info("Token reissued success");
//                    refreshPage(response);
                } else {
                    log.info("refresh token expired");
                    removeCookie(response);
//                    refreshPage(response);
                }
            } catch (Exception e) {
                log.info("Token reissued failed");
                removeCookie(response);
//                refreshPage(response);
            }
            refreshPage(request, response);
        }
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

//    private void refreshPage(HttpServletResponse response) {
//        try {
//            response.setContentType("text/html");
//            PrintWriter out = response.getWriter();
//            out.println("<html><body>");
//            out.println("<script type=\"text/javascript\">");
//            out.println("window.location.reload(true);");
//            out.println("</script>");
//            out.println("</body></html>");
//            out.close();
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }

    private void refreshPage(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<form id=\"resubmitForm\" method=\"" + request.getMethod() + "\" action=\"" + request.getRequestURI() + "\">");

            // Add request parameters to the form
            request.getParameterMap().forEach((key, values) -> {
                for (String value : values) {
                    out.println("<input type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\">");
                }
            });

            out.println("</form>");
            out.println("<script type=\"text/javascript\">");
            // 새로운 access 토큰을 가져와서 헤더에 추가
            out.println("var form = document.getElementById('resubmitForm');");
            out.println("var xhr = new XMLHttpRequest();");
            out.println("xhr.open(form.method, form.action, true);");
            out.println("xhr.setRequestHeader('access', '" + CookieUtils.getCookieValue(request, "access") + "');");
            out.println("xhr.onload = function() {");
            out.println("  document.open();");
            out.println("  document.write(xhr.responseText);");
            out.println("  document.close();");
            out.println("};");
            out.println("xhr.send(new FormData(form));");
            out.println("</script>");
            out.println("</body></html>");
            out.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}