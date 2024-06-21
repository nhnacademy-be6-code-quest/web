package com.nhnacademy.codequestweb.config;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.interceptor.LoginInfoInterceptor;
import com.nhnacademy.codequestweb.interceptor.TokenReissueInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthClient authClient;

    public WebConfig(@Lazy AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInfoInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new TokenReissueInterceptor(authClient))
                .addPathPatterns("/mypage");
    }

}