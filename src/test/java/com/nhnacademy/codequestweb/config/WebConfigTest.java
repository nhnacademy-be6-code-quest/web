package com.nhnacademy.codequestweb.config;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.interceptor.CategoryInterceptor;
import com.nhnacademy.codequestweb.interceptor.LoginInfoInterceptor;
import com.nhnacademy.codequestweb.interceptor.TokenReissueInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WebConfigTest {
    @Mock
    private AuthClient authClient;

    @Mock
    private CategoryConfig categoryConfig;

    private WebConfig webConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webConfig = new WebConfig(authClient, categoryConfig);
    }

    @Test
    void testAddInterceptors() {
        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        InterceptorRegistration loginInfoRegistration = mock(InterceptorRegistration.class);
        InterceptorRegistration tokenReissueRegistration = mock(InterceptorRegistration.class);
        InterceptorRegistration categoryRegistration = mock(InterceptorRegistration.class);

        when(registry.addInterceptor(any(LoginInfoInterceptor.class))).thenReturn(loginInfoRegistration);
        when(registry.addInterceptor(any(TokenReissueInterceptor.class))).thenReturn(tokenReissueRegistration);
        when(registry.addInterceptor(any(CategoryInterceptor.class))).thenReturn(categoryRegistration);

        webConfig.addInterceptors(registry);

        // Verify the interceptors are added with correct path patterns
        verify(registry).addInterceptor(any(LoginInfoInterceptor.class));
        verify(loginInfoRegistration).addPathPatterns("/**");

        verify(registry).addInterceptor(any(TokenReissueInterceptor.class));
        verify(tokenReissueRegistration).addPathPatterns("/**");

        verify(registry).addInterceptor(any(CategoryInterceptor.class));
        verify(categoryRegistration).addPathPatterns("/**");
    }

    @Test
    void testMessageSource() {
        MessageSource messageSource = webConfig.messageSource();

        assertThat(messageSource).isInstanceOf(ResourceBundleMessageSource.class);
    }
}
