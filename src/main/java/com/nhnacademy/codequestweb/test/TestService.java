package com.nhnacademy.codequestweb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TestService {


    @Autowired
    private TestClient testClient;


    public Page<ClientCouponPaymentResponseDto> getClient(HttpHeaders httpHeaders, int page, int size){
        return testClient.getCouponClient(httpHeaders, page,size).getBody();

    }
}
