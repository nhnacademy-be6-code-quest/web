package com.nhnacademy.codequestweb.interceptor;

import com.nhnacademy.codequestweb.config.CategoryConfig;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryNodeResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CategoryInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CategoryConfig categoryConfig;

    private CategoryInterceptor categoryInterceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryInterceptor = new CategoryInterceptor(categoryConfig);
    }

    @Test
    void testPreHandle_CategoryRootNotNull() throws Exception {
        CategoryNodeResponseDto mockRoot = new CategoryNodeResponseDto();
        when(categoryConfig.getRoot()).thenReturn(mockRoot);

        boolean result = categoryInterceptor.preHandle(request, response, new Object());

        verify(categoryConfig, never()).init();
        verify(request).setAttribute("categories", mockRoot);
        assertTrue(result);
    }

    @Test
    void testPreHandle_CategoryRootIsNull() throws Exception {
        when(categoryConfig.getRoot()).thenReturn(null);

        boolean result = categoryInterceptor.preHandle(request, response, new Object());

        verify(categoryConfig).init();
        verify(request).setAttribute("categories", null); // since init doesn't set the root in this test case
        assertTrue(result);
    }
}
