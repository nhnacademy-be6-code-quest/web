package com.nhnacademy.codequestweb.config;

import com.nhnacademy.codequestweb.client.product.category.CategoryClient;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryNodeResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryConfigTest {

    @Mock
    private CategoryClient categoryClient;

    private CategoryConfig categoryConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitSuccess() {
        CategoryNodeResponseDto mockRoot = new CategoryNodeResponseDto();
        when(categoryClient.getCategoriesTree()).thenReturn(ResponseEntity.ok(mockRoot));

        categoryConfig = new CategoryConfig(categoryClient);

        assertNotNull(categoryConfig.getRoot());
        assertEquals(mockRoot, categoryConfig.getRoot());
        verify(categoryClient, times(1)).getCategoriesTree();
    }

    @Test
    void testInitFailure() {
        when(categoryClient.getCategoriesTree()).thenThrow(new RuntimeException("API Error"));

        categoryConfig = new CategoryConfig(categoryClient);

        assertNull(categoryConfig.getRoot());
        verify(categoryClient, times(1)).getCategoriesTree();
    }

    @Test
    void testUpdate() {
        categoryConfig = new CategoryConfig(categoryClient);

        CategoryNodeResponseDto newRoot = new CategoryNodeResponseDto();
        categoryConfig.update(newRoot);

        assertEquals(newRoot, categoryConfig.getRoot());
    }
}