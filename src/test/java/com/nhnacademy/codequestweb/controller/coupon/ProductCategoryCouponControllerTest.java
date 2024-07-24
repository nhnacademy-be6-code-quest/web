package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.service.product.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryCouponControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductCategoryCouponController productCategoryCouponController;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategoriesPage() {
        Page<CategoryGetResponseDto> mockPage = new PageImpl<>(List.of(
                new CategoryGetResponseDto(1L, "Category1", null, List.of()),
                new CategoryGetResponseDto(2L, "Category2", new ProductCategory(1L, "Category1", null), List.of())
        ));
        when(categoryService.getCategories(anyInt(), anyBoolean(), anyString())).thenReturn(ResponseEntity.ok(mockPage));

        String result = productCategoryCouponController.getAllCategoriesPage(0, false, "name", model);

        assertEquals("view/coupon/categoryAdd", result);
        verify(model).addAttribute(eq("categoryNamePage"), anyMap());
        verify(model).addAttribute("register", false);
    }

    @Test
    void testGetCategoryContainingPage() {
        Page<CategoryGetResponseDto> mockPage = new PageImpl<>(List.of(
                new CategoryGetResponseDto(1L, "TestCategory", null, List.of())
        ));
        when(categoryService.getNameContainingCategories(anyInt(), anyBoolean(), anyString(), anyString())).thenReturn(ResponseEntity.ok(mockPage));

        String result = productCategoryCouponController.getCategoryContainingPage(0, false, "name", "Test", model);

        assertEquals("view/coupon/categoryAdd", result);
        verify(model).addAttribute(eq("categoryNamePage"), anyMap());
        verify(model).addAttribute("register", false);
    }

    @Test
    void testGetCategorySubPage() {
        Page<CategoryGetResponseDto> mockPage = new PageImpl<>(List.of(
                new CategoryGetResponseDto(2L, "SubCategory", new ProductCategory(1L, "ParentCategory", null), List.of())
        ));
        when(categoryService.getSubCategories(anyInt(), anyBoolean(), anyString(), anyLong())).thenReturn(ResponseEntity.ok(mockPage));

        String result = productCategoryCouponController.getCategorySubPage(0, false, "name", 1L, model);

        assertEquals("view/coupon/categoryAdd", result);
        verify(model).addAttribute(eq("categoryNamePage"), anyMap());
        verify(model).addAttribute("register", true);
    }
}