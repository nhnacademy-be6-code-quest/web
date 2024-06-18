package com.nhnacademy.codequestweb.controller.payment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {

    @GetMapping("/payment")
    public String payment() {
        return "view/payment/payment";
    }
}