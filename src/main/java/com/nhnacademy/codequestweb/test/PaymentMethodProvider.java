package com.nhnacademy.codequestweb.test;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodProvider {

    private final Map<String, String> viewPath;

    public PaymentMethodProvider() {
        this.viewPath = new HashMap<>();
        viewPath.put("toss", "view/payment/tossPage");
        viewPath.put("naver", "view/payment/naverPage");
        viewPath.put("kakao", "view/payment/kakaoPage");
    }

    public String getViewPath(String methodName) {
        return viewPath.get(methodName);
    }


}
