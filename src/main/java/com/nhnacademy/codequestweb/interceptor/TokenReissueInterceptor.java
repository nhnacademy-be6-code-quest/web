package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
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
                    accessCookie.setMaxAge(60 * 60 * 2);
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

            // Add original headers to the form
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                Enumeration<String> headers = request.getHeaders(headerName);
                while (headers.hasMoreElements()) {
                    String headerValue = headers.nextElement();
                    out.println("<input type=\"hidden\" name=\"header_" + headerName + "\" value=\"" + headerValue + "\">");
                }
            }

            out.println("</form>");
            out.println("<script type=\"text/javascript\">");
            out.println("document.getElementById('resubmitForm').submit();");
            out.println("</script>");
            out.println("</body></html>");
            out.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}