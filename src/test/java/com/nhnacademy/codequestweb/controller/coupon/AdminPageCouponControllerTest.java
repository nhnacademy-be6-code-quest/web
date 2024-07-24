package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
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

class AdminPageCouponControllerTest {

    @Mock
    private CouponService couponService;

    @InjectMocks
    private AdminPageCouponController adminPageCouponController;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access", "access")});
    }

    @Test
    void testViewAdminCoupon() {
        Page mockPage = new PageImpl<>(new ArrayList<>());
        when(couponService.findUsersCoupons(any(HttpHeaders.class), anyInt(), anyInt(), any(Status.class))).thenReturn(mockPage);

        String result = adminPageCouponController.viewAdminCoupon(request, 0, 6, Status.AVAILABLE);

        assertEquals("index", result);
        verify(request).setAttribute("view", "adminPage");
        verify(request).setAttribute("adminPage", "adminCoupons");
        verify(request).setAttribute("activeSection", "coupon");
        verify(request).setAttribute("adminCoupons", mockPage);
        verify(request).setAttribute("totalPages", mockPage.getTotalPages());
        verify(request).setAttribute("currentPage", mockPage.getNumber());
        verify(request).setAttribute("pageSize", mockPage.getSize());
        verify(request).setAttribute("status", Status.AVAILABLE);
    }

    @Test
    void testViewActiveUserCoupons() {
        Page mockPage = new PageImpl<>(new ArrayList<>());
        when(couponService.findUsersCoupons(any(HttpHeaders.class), anyInt(), anyInt(), eq(Status.AVAILABLE))).thenReturn(mockPage);

        String result = adminPageCouponController.viewActiveUserCoupons(request, 0, 6);

        assertEquals("index", result);
        verify(request).setAttribute("status", Status.AVAILABLE);
    }

    @Test
    void testViewExpiredUserCoupons() {
        Page mockPage = new PageImpl<>(new ArrayList<>());
        when(couponService.findUsersCoupons(any(HttpHeaders.class), anyInt(), anyInt(), eq(Status.UNAVAILABLE))).thenReturn(mockPage);

        String result = adminPageCouponController.viewExpiredUserCoupons(request, 0, 6);

        assertEquals("index", result);
        verify(request).setAttribute("status", Status.UNAVAILABLE);
    }

    @Test
    void testViewUsedUserCoupons() {
        Page mockPage = new PageImpl<>(new ArrayList<>());
        when(couponService.findUsersCoupons(any(HttpHeaders.class), anyInt(), anyInt(), eq(Status.USED))).thenReturn(mockPage);

        String result = adminPageCouponController.viewUsedUserCoupons(request, 0, 6);

        assertEquals("index", result);
        verify(request).setAttribute("status", Status.USED);
    }
}