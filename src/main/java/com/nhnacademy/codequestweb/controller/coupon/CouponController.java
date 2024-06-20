package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.domain.entity.Coupon;
import com.nhnacademy.codequestweb.domain.entity.CouponPolicy;
import com.nhnacademy.codequestweb.request.coupon.CouponRequestDto;
import com.nhnacademy.codequestweb.response.auth.coupon.CouponPolicyResponseDto;
import com.nhnacademy.codequestweb.response.auth.coupon.CouponTypeResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponPolicyService;
import com.nhnacademy.codequestweb.service.coupon.CouponTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Controller
public class CouponController {
    //@Autowired
   // private CouponService couponService;
    @Autowired
    private CouponPolicyService couponPolicyService;
    @Autowired
    private CouponTypeService couponTypeService;

    @PostMapping("/admin/coupon/register")
    public String saveCoupon(@RequestBody CouponRequestDto couponRequestDto, Model model){
        List<CouponTypeResponseDto> couponTypes = couponTypeService.getAllCouponTypes();
        List<CouponPolicyResponseDto> couponPolicies = couponPolicyService.getAllCouponPolicies();
        List<Status> statuses = List.of(Status.AVAILABLE, Status.USED,Status.UNAVAILABLE);
       // CouponRequestDto coupon = couponService.saveCoupon(couponRequestDto);
       // model.addAttribute("coupon",coupon);
        model.addAttribute("couponType",couponTypes);
        model.addAttribute("couponPolicy",couponPolicies);
        model.addAttribute("status",statuses);
        return "/view/coupon/register_coupon";

    }
}
