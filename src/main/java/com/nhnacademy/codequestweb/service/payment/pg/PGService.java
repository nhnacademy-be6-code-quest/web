package com.nhnacademy.codequestweb.service.payment.pg;

import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;


public interface PGService {
    String getPaymentViewPath();
    void setPaymentViewModel(HttpHeaders headers, String orderCode, String pgName, Model model);
}
