package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;
import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyListResponseDto;
import com.nhnacademy.codequestweb.response.coupon.ProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.service.coupon.ClientCouponService;
import com.nhnacademy.codequestweb.service.coupon.CouponPolicyService;
import com.nhnacademy.codequestweb.service.product.CategoryService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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

    private final CouponPolicyService couponPolicyService;
    private final ClientCouponService clientCouponService;
    private final CategoryService categoryService;



    @GetMapping("/coupon/books")
    public String bookView( @RequestParam(name = "page", required = false)Integer page,
                            @RequestParam(name = "sort", required = false)String sort,
                            @RequestParam(name = "desc", required = false)Boolean desc ,
                            Model model){

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id","1");
        headers.set("X-User-Role","ROLE_ADMIN");

        Page<ProductGetResponseDto> books = clientCouponService.getAllBooks(headers, page ,sort, desc);
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




    @GetMapping("/admin/coupon/policy")
    public String viewPolicy(HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){
        if (CookieUtils.getCookieValue(req, "refresh") == null) {
            return "redirect:/auth";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        Page<CouponPolicyListResponseDto> couponPolicies = couponPolicyService.getAllCouponPolicies(page, size);
        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "couponPolicy");

        req.setAttribute("couponPolicies",couponPolicies);
        return "index";
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
        return "redirect:/admin/coupon/policy";
    }

}
