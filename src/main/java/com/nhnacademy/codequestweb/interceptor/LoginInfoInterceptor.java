package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CookieUtils.getCookieValue(request, "refresh") != null) {
            request.setAttribute("isLogin", "true");
        }
        return true;
    }
}
