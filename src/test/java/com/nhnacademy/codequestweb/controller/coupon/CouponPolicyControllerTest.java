package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;
import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyListResponseDto;
import com.nhnacademy.codequestweb.response.coupon.ProductGetResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponPolicyService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CouponPolicyControllerTest {

    @Mock
    private CouponPolicyService couponPolicyService;

    @InjectMocks
    private CouponPolicyController couponPolicyController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access", "access")});
    }

    @Test
    void testBookView() {
        Page<ProductGetResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(couponPolicyService.getAllBooks(any(HttpHeaders.class), anyInt(), anyString(), anyBoolean())).thenReturn(mockPage);

        String result = couponPolicyController.bookView(request, 0, "title", true, model);

        assertEquals("view/coupon/productAdd", result);
        verify(model).addAttribute("books", mockPage);
    }

    @Test
    void testViewPolicy() {
        Page<CouponPolicyListResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(couponPolicyService.getAllCouponPolicies(any(HttpHeaders.class), anyInt(), anyInt())).thenReturn(mockPage);

        String result = couponPolicyController.viewPolicy(request, 0, 6);

        assertEquals("index", result);
        verify(request).setAttribute("view", "adminPage");
        verify(request).setAttribute("adminPage", "couponPolicy");
        verify(request).setAttribute("activeSection", "coupon");
        verify(request).setAttribute("couponPolicies", mockPage);
    }

    @Test
    void testViewRegisterPolicy() {
        String result = couponPolicyController.viewRegisterPolicy(request);

        assertEquals("view/coupon/admin_policy_register", result);
        verify(request).setAttribute("view", "couponPolicy");
        verify(request).setAttribute("discountTypes", List.of(DiscountType.AMOUNTDISCOUNT, DiscountType.PERCENTAGEDISCOUNT));
    }

    @Test
    void testRegisterPolicy() {
        CouponPolicyRegisterRequestDto mockDto = new CouponPolicyRegisterRequestDto("", DiscountType.PERCENTAGEDISCOUNT, 1L, 1L, 1L, 1L, "");

        String result = couponPolicyController.registerPolicy(request, mockDto);

        assertEquals("redirect:/admin/coupon/policy", result);
        verify(couponPolicyService).savePolicy(any(HttpHeaders.class), eq(mockDto));
    }
}