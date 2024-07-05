package com.nhnacademy.codequestweb.config;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.client.product.category.CategoryClient;
import com.nhnacademy.codequestweb.interceptor.CategoryInterceptor;
import com.nhnacademy.codequestweb.interceptor.LoginInfoInterceptor;
import com.nhnacademy.codequestweb.interceptor.TokenReissueInterceptor;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthClient authClient;
    private final CategoryConfig categoryConfig;

    public WebConfig(@Lazy AuthClient authClient, @Lazy CategoryConfig categoryConfig) {
        this.authClient = authClient;
        this.categoryConfig = categoryConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInfoInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new TokenReissueInterceptor(authClient))
                .addPathPatterns("/**");
        registry.addInterceptor(new CategoryInterceptor(categoryConfig))
                .addPathPatterns("/**");
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}