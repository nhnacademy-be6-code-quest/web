package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.response.coupon.CouponMyPageCouponResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage/coupons")
public class MyPageCouponController {


    private final CouponService couponService;

    @GetMapping
    public String viewUserCoupon(HttpServletRequest req,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam (defaultValue = "6") int size,
        @RequestParam(defaultValue = "AVAILABLE")
        Status status) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        Page<CouponMyPageCouponResponseDto> coupons = couponService.findMyPageCoupons(headers, page, size, status);
        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "coupons");
        req.setAttribute("activeSection", "coupon");
        req.setAttribute("coupons", coupons);
        req.setAttribute("totalPages", coupons.getTotalPages());
        req.setAttribute("currentPage", coupons.getNumber());
        req.setAttribute("pageSize", coupons.getSize());
        req.setAttribute("status",status);
        return "index";
    }

    @GetMapping("/AVAILABLE")
    public String viewActiveCoupons(HttpServletRequest req,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size) {
        return viewUserCoupon(req, page, size, Status.AVAILABLE);
    }

    @GetMapping("/expired")
    public String viewExpiredCoupons(HttpServletRequest req,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size) {
        return viewUserCoupon(req, page, size, Status.UNAVAILABLE);
    }

    @GetMapping("/used")
    public String viewUsedCoupons(HttpServletRequest req,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size) {
        return viewUserCoupon(req, page, size, Status.USED);
    }
}
