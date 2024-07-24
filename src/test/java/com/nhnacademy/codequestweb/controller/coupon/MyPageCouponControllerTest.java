package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.response.coupon.CouponMyPageCouponResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
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

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MyPageCouponControllerTest {

    @Mock
    private CouponService couponService;

    @InjectMocks
    private MyPageCouponController myPageCouponController;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access", "access")});
    }

    @Test
    void testViewUserCoupon() {
        Page<CouponMyPageCouponResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(couponService.findMyPageCoupons(any(HttpHeaders.class), anyInt(), anyInt(), any(Status.class))).thenReturn(mockPage);

        String result = myPageCouponController.viewUserCoupon(request, 0, 6, Status.AVAILABLE);

        assertEquals("index", result);
        verify(request).setAttribute("view", "mypage");
        verify(request).setAttribute("mypage", "coupons");
        verify(request).setAttribute("activeSection", "coupon");
        verify(request).setAttribute("coupons", mockPage);
        verify(request).setAttribute("totalPages", mockPage.getTotalPages());
        verify(request).setAttribute("currentPage", mockPage.getNumber());
        verify(request).setAttribute("pageSize", mockPage.getSize());
        verify(request).setAttribute("status", Status.AVAILABLE);
    }

    @Test
    void testViewActiveCoupons() {
        Page<CouponMyPageCouponResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(couponService.findMyPageCoupons(any(HttpHeaders.class), anyInt(), anyInt(), eq(Status.AVAILABLE))).thenReturn(mockPage);

        String result = myPageCouponController.viewActiveCoupons(request, 0, 6);

        assertEquals("index", result);
        verify(request).setAttribute("status", Status.AVAILABLE);
    }

    @Test
    void testViewExpiredCoupons() {
        Page<CouponMyPageCouponResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(couponService.findMyPageCoupons(any(HttpHeaders.class), anyInt(), anyInt(), eq(Status.UNAVAILABLE))).thenReturn(mockPage);

        String result = myPageCouponController.viewExpiredCoupons(request, 0, 6);

        assertEquals("index", result);
        verify(request).setAttribute("status", Status.UNAVAILABLE);
    }

    @Test
    void testViewUsedCoupons() {
        Page<CouponMyPageCouponResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(couponService.findMyPageCoupons(any(HttpHeaders.class), anyInt(), anyInt(), eq(Status.USED))).thenReturn(mockPage);

        String result = myPageCouponController.viewUsedCoupons(request, 0, 6);

        assertEquals("index", result);
        verify(request).setAttribute("status", Status.USED);
    }
}