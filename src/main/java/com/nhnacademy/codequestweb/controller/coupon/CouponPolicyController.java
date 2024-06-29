package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;
import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.service.coupon.ClientCouponService;
import com.nhnacademy.codequestweb.service.coupon.CouponPolicyService;
import com.nhnacademy.codequestweb.service.product.CategoryService;
import com.nhnacademy.codequestweb.test.ProductGetResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CouponPolicyController {
    private static final int DEFAULT_PAGE_SIZE = 5;

    private final CouponPolicyService couponPolicyService;
    private final ClientCouponService clientCouponService;
    private final CategoryService categoryService;



    @GetMapping("/coupon/books")
    public String bookView(PageRequestDto pageRequestDto, Model model){
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("X-User-Id","1");
//        httpHeaders.set("X-User-Role","ROLE_ADMIN");

        Page<ProductGetResponseDto> books = clientCouponService.getAllBookPage(pageRequestDto);
        model.addAttribute("books",books);
        return "/view/coupon/testbook";
    }

    @GetMapping("/coupon/categories")
    public String categoryview(@RequestParam(name = "page", required = false)Integer page,
                       @RequestParam(name = "sort", required = false)String sort,
                       @RequestParam(name = "desc", required = false)Boolean desc ,
                       Model model){

        ResponseEntity<Page<CategoryGetResponseDto>> categories = categoryService.getCategories(page,desc, sort );
        model.addAttribute("categories",categories.getBody());
        return "/view/coupon/testcategory";
    }

    @GetMapping("/api/coupon/policy")
    public String viewPolicy(Model model, Pageable pageable){
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE);
        Page<CouponPolicyResponseDto> coupPolicies = couponPolicyService.getAllCouponPolicies(pageRequest);



        model.addAttribute("couponPolicies",coupPolicies);
        return "/view/coupon/admin_policy_list";
    }
    @GetMapping("/api/coupon/policy/register")
    public String viewRegisterPolicy(Model model){
        List<DiscountType> discountTypes = List.of(DiscountType.AMOUNTDISCOUNT,DiscountType.PERCENTAGEDISCOUNT);
        model.addAttribute("discountTypes",discountTypes);
        return "/view/coupon/admin_policy_register";
    }
    @PostMapping("/api/coupon/policy/register")
    public String registerPolicy(@Valid @ModelAttribute CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto){
        couponPolicyService.savePolicy(couponPolicyRegisterRequestDto);
        return "redirect:/api/coupon/policy";
    }

}
