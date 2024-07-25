package com.nhnacademy.codequestweb.test;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodProvider {

    private final Map<String, String> method;
    private Map<String, String> paymentKey;

    public PaymentMethodProvider() {
        this.method = new HashMap<>();
        method.put("toss", "view/payment/tossPage");
        method.put("naver", "view/payment/naverPage");
        method.put("kakao", "view/payment/kakaoPage");
    }

    public String getName(String methodName) {
        return method.get(methodName);
    }

    public Map<String, String> PaymentKey(String id, String value){
        paymentKey.put(id,value);
        return paymentKey;
    }
}
