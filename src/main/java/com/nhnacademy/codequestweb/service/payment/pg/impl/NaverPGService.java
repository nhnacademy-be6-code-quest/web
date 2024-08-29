package com.nhnacademy.codequestweb.service.payment.pg.impl;

import com.nhnacademy.codequestweb.request.payment.NaverPaymentViewRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentViewRequestDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.service.payment.pg.PGService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service("naver")
@RequiredArgsConstructor
public class NaverPGService implements PGService {

    private final PaymentService paymentService;

    @Override
    public String getPaymentViewPath() {
        return "view/payment/naverPage";
    }

    @Override
    public void setPaymentViewModel(HttpHeaders headers, String orderCode, String pgName, Model model) {
        PaymentViewRequestDto paymentViewRequestDto = paymentService.getPaymentViewRequest(headers, orderCode, pgName);
        if(paymentViewRequestDto instanceof NaverPaymentViewRequestDto naverPaymentViewRequestDto){
            model.addAttribute("orderCode", orderCode);
            model.addAttribute("productName", naverPaymentViewRequestDto.getProductName());
            model.addAttribute("merchantPayKey", naverPaymentViewRequestDto.getOrderCode());
            model.addAttribute("totalPayAmount", naverPaymentViewRequestDto.getAmount());
            model.addAttribute("taxScopeAmount", naverPaymentViewRequestDto.getTaxScopeAmount());
            model.addAttribute("taxExScopeAmount", naverPaymentViewRequestDto.getTaxExScopeAmount());
            model.addAttribute("returnUrl", getSuccessUrl(orderCode));

        }
    }

    private String getSuccessUrl(String orderCode) {
        String url = null;
        try{
            url = URLEncoder.encode(String.format("https://localhost:8080/client/order/%s/payment/naver/success", orderCode), StandardCharsets.UTF_8.toString());
        }
        catch (UnsupportedEncodingException e){
            log.warn("url 인코딩 중 오류가 발생하였습니다.");
        }
        return url;
    }

    private String getFailUrl() {
        return null;
    }
}
