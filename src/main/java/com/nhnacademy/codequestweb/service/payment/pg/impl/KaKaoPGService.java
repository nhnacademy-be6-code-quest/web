package com.nhnacademy.codequestweb.service.payment.pg.impl;

import com.nhnacademy.codequestweb.request.payment.viewRequest.impl.KakaoPaymentViewRequestDto;
import com.nhnacademy.codequestweb.request.payment.viewRequest.PaymentViewRequestDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.service.payment.pg.PGService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service("kakao")
@RequiredArgsConstructor
public class KaKaoPGService implements PGService {

    private final PaymentService paymentService;

    @Override
    public String getPaymentViewPath() {
        return "view/payment/kakaoPage";
    }

    @Override
    public void setPaymentViewModel(HttpHeaders headers, String orderCode, String pgName, Model model) {
        PaymentViewRequestDto paymentViewRequestDto = paymentService.getPaymentViewRequest(headers, orderCode, pgName);
        if(paymentViewRequestDto instanceof KakaoPaymentViewRequestDto kakaoPaymentViewRequestDto){
            model.addAttribute("redirectUrl", kakaoPaymentViewRequestDto.getRedirectUrl());
        }
    }

}
