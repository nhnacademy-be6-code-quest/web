package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.ClientCouponClient;
import com.nhnacademy.codequestweb.response.coupon.ClientCouponPaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClientCouponService {


    @Autowired
    private ClientCouponClient clientCouponClient;


    public Page<ClientCouponPaymentResponseDto> getClient(HttpHeaders httpHeaders, int page, int size){
        return clientCouponClient.getCouponClient(httpHeaders, page,size).getBody();

    }
}
