package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.request.coupon.CouponRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.*;
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
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CouponControllerTest {

    @Mock
    private CouponService couponService;

    @InjectMocks
    private CouponController couponController;

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
    void testView() {
        Page<ClientCouponPaymentResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(couponService.getClient(any(HttpHeaders.class), anyInt(), anyInt())).thenReturn(mockPage);

        String result = couponController.view(request, model, 0, 10);

        assertEquals("view/coupon/coupon_client", result);
        verify(model).addAttribute("couponPayments", mockPage);
    }

    @Test
    void testSaveCouponView() {
        List<CouponTypeResponseDto> mockCouponTypes = new ArrayList<>();
        CouponProvideTypeResponseDto mockName = new CouponProvideTypeResponseDto();
        when(couponService.getAllCouponTypes(any(HttpHeaders.class))).thenReturn(mockCouponTypes);
        when(couponService.findCouponType(any(HttpHeaders.class), anyLong())).thenReturn(mockName);

        String result = couponController.saveCouponView(request, model, 1L);

        assertEquals("view/coupon/admin_coupon_register", result);
        verify(model).addAttribute("couponTypes", mockCouponTypes);
        verify(model).addAttribute("status", List.of(Status.AVAILABLE, Status.USED, Status.UNAVAILABLE));
        verify(model).addAttribute("couponPolicyId", 1L);
        verify(model).addAttribute("name", mockName);
    }

    @Test
    void testSaveCoupon() {
        CouponRegisterRequestDto mockDto = new CouponRegisterRequestDto(1L, 1L, List.of(), LocalDate.now(), Status.USED);

        String result = couponController.saveCoupon(request, 1L, mockDto);

        assertEquals("redirect:/admin/coupon/policy", result);
        verify(couponService).saveCoupon(any(HttpHeaders.class), eq(mockDto), eq(1L));
    }
}