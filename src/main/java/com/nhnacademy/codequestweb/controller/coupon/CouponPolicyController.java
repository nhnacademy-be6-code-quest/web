package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;
import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRequestDto;
import com.nhnacademy.codequestweb.response.auth.coupon.CouponPolicyResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class CouponPolicyController {
    private static final int DEFAULT_PAGE_SIZE = 5;
    @Autowired
    private CouponPolicyService couponPolicyService;

    @GetMapping("/admin/coupon/policy")
    public String viewPolicy(Model model, Pageable pageable){
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE);
        Page<CouponPolicyResponseDto> coupPolicies = couponPolicyService.getAllCouponPolicies(pageRequest);
        model.addAttribute("couponPolicies",coupPolicies);
        return "/view/coupon/list_couponPolicy";
    }
    @GetMapping("/admin/coupon/policy/register")
    public String viewRegisterPolicy(Model model){
        List<DiscountType> discountTypes = List.of(DiscountType.AMOUNTDISCOUNT,DiscountType.PERCENTAGEDISCOUNT);
        model.addAttribute("discountTypes",discountTypes);
        return "/view/coupon/coupon_policy_register";
    }
    @PostMapping("/admin/coupon/policy/register")
    public String registerPolicy(@ModelAttribute CouponPolicyRequestDto couponPolicyRequestDto){
        couponPolicyService.savePolicy(couponPolicyRequestDto);
        return "redirect:/admin/coupon/policy";
    }
}
