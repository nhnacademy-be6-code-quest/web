package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.coupon.CouponRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponTypeResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponPolicyService;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.service.coupon.CouponTypeService;
import com.nhnacademy.codequestweb.test.Client;
import com.nhnacademy.codequestweb.test.ClientCouponPaymentResponseDto;
import com.nhnacademy.codequestweb.test.TestClient;
import com.nhnacademy.codequestweb.test.TestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;



@Controller
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponPolicyService couponPolicyService;
    @Autowired
    private CouponTypeService couponTypeService;


    @Autowired
    private TestService testService;




    @GetMapping("/api/client/{clientId}")
    public String viewCoupon(@PathVariable long clientId, Model model){
        List<CouponResponseDto> couponList = couponService.findClientCoupon(clientId);
        model.addAttribute("couponList",couponList);
        return "/view/coupon/client_coupon_view";
    }

    @GetMapping("/processUserSelection")
    public String view(
            Model model, @RequestParam(defaultValue = "10") int page, @RequestParam(defaultValue = "0") int size){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("email","hi");
        httpHeaders.set("role","ROLE_ADMIN");


        //httpHeaders.set("access", CookieUtils);
        //httpHeaders.set("refesh",CookiUtils);
        Page<ClientCouponPaymentResponseDto> coupons = testService.getClient(httpHeaders, size, page);
        model.addAttribute("couponPayments",coupons);
        return "/view/coupon/test";
    }

    @GetMapping("/api/coupon/register/{couponPolicyId}")
    public String saveCouponView(Model model, @PathVariable long couponPolicyId, HttpSession httpSession){
        List<CouponTypeResponseDto> couponTypes = couponTypeService.getAllCouponTypes();
        couponPolicyService.getCouponPolicy(couponPolicyId);

        List<Status> statuses = List.of(Status.AVAILABLE, Status.USED,Status.UNAVAILABLE);
        model.addAttribute("couponTypes",couponTypes);
        model.addAttribute("status",statuses);
        model.addAttribute("couponPolicyId",couponPolicyId);
        return "/view/coupon/admin_coupon_register";
    }

    @PostMapping("/api/coupon/register/{couponPolicyId}")
    public String saveCoupon(@Valid @PathVariable long couponPolicyId, @ModelAttribute CouponRequestDto couponRequestDto){

      couponService.saveCoupon(couponRequestDto,couponPolicyId);
        return "redirect:/api/coupon/policy";

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String validationError(MethodArgumentNotValidException e, HttpServletRequest req) {
        CouponRequestDto requestDto = getRequestDto(req);

        req.setAttribute("register_message", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return "index";
    }
    private CouponRequestDto getRequestDto(HttpServletRequest req){
        CouponRequestDto requestDto = new CouponRequestDto(Long.parseLong(req.getParameter("couponTypeId")), Long.parseLong(req.getParameter("couponPolicyId")), Long.parseLong(req.getParameter("clientId")), LocalDateTime.parse(req.getParameter("expirationDate")), Status.valueOf(req.getParameter("status")));
        return requestDto;
    }
}
