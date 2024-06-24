package com.nhnacademy.codequestweb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    @Autowired
    private TestClient testClient;


    public Page<ClientCouponPaymentResponseDto> getClient(int size, int page){
        return testClient.getCouponClient(size,page).getBody();

    }
}
