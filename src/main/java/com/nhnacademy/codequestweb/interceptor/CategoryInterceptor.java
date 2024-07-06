package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.config.CategoryConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class CategoryInterceptor implements HandlerInterceptor {
    private final CategoryConfig categoryConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (categoryConfig.getRoot() == null) {
            categoryConfig.init();
        }
        request.setAttribute("categories", categoryConfig.getRoot());
        return true;
    }
}
