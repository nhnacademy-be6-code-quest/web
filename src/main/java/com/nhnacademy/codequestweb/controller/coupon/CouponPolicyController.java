package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;
import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyListResponseDto;
import com.nhnacademy.codequestweb.response.coupon.ProductGetResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponPolicyService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class CouponPolicyController {
    private static final String ACCESS = "access";

    private final CouponPolicyService couponPolicyService;


    @GetMapping("/admin/coupon/books")
    public String bookView(
        HttpServletRequest req,
        @RequestParam(name = "page", required = false) Integer page,
        @RequestParam(name = "sort", required = false) String sort,
        @RequestParam(name = "desc", required = false) Boolean desc,
        Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));
        Page<ProductGetResponseDto> books = couponPolicyService.getAllBooks(headers, page, sort, desc);
        model.addAttribute("books", books);
        req.setAttribute("totalPages", books.getTotalPages());
        req.setAttribute("currentPage", books.   getNumber());
        req.setAttribute("pageSize", books.getSize());
        return "view/coupon/productAdd";
    }

    @GetMapping("/admin/coupon/policy")
    public String viewPolicy(HttpServletRequest req, @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));

        Page<CouponPolicyListResponseDto> couponPolicies = couponPolicyService.getAllCouponPolicies(headers,
            page, size);
        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "couponPolicy");
        req.setAttribute("activeSection", "coupon");
        req.setAttribute("couponPolicies", couponPolicies);
        req.setAttribute("totalPages", couponPolicies.getTotalPages());
        req.setAttribute("currentPage", couponPolicies.   getNumber());
        req.setAttribute("pageSize", couponPolicies.getSize());
        return "index";
    }

    @GetMapping("/admin/coupon/policy/register")
    public String viewRegisterPolicy(HttpServletRequest req) {
        List<DiscountType> discountTypes = List.of(DiscountType.AMOUNTDISCOUNT,
            DiscountType.PERCENTAGEDISCOUNT);
        req.setAttribute("view", "couponPolicy");
        req.setAttribute( "discountTypes", discountTypes);
        return "view/coupon/admin_policy_register";
    }

    @PostMapping("/admin/coupon/policy/register")
    public String registerPolicy(HttpServletRequest req,

        @Valid @ModelAttribute CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));
        couponPolicyService.savePolicy(headers, couponPolicyRegisterRequestDto);
        return "redirect:/admin/coupon/policy";
    }

}
