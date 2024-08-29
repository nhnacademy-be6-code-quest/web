package com.nhnacademy.codequestweb.service.payment.pg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PGServiceProvider {

    private final Map<String, PGService> pgServiceMap;

    public String getPaymentViewPath(String paymentMethod){
        paymentMethod = paymentMethod.toLowerCase();
        return pgServiceMap.get(paymentMethod).getPaymentViewPath();
    }

    public void setPaymentViewModel(String paymentMethod, HttpHeaders headers, String orderCode, Model model){
        paymentMethod = paymentMethod.toLowerCase();
        pgServiceMap.get(paymentMethod).setPaymentViewModel(headers, orderCode, paymentMethod, model);
    }

}
