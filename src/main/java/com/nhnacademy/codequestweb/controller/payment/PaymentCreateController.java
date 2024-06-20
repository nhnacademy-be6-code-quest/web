package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.temp.Coupon;
import com.nhnacademy.codequestweb.temp.TempData;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 테스트입니다...
 *
 * @author : Virtus_Chae
 * @version : 1.o
 */
@Controller
public class PaymentCreateController {

//    @GetMapping("/payment")
//    public String payment(Model model, TempData tempData) {
//        Coupon coupon = new Coupon(1L, "CouponExample");
//        List<Coupon> coupons = new ArrayList<>();
//        coupons.add(coupon);
//        tempData = new TempData(100L, coupons, 100000L, 90000L, 9000L);
//        model.addAttribute("coupons", coupons);
//        model.addAttribute("tempData", tempData);
//        return "createPayment";
//    }
}