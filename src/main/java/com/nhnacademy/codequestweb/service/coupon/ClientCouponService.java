package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.ClientCouponClient;
import com.nhnacademy.codequestweb.client.product.bookProduct.BookProductClient;
import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.response.coupon.ClientCouponPaymentResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.test.ProductGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


@Service
@RequiredArgsConstructor
public class ClientCouponService {


    @Autowired
    private ClientCouponClient clientCouponClient;



    public Page<ProductGetResponseDto> getAllBookPage(
            HttpHeaders headers,
            PageRequestDto pageRequestDto
    ){
        return clientCouponClient.getAllProducts(headers, pageRequestDto).getBody();


    }

    public Page<ClientCouponPaymentResponseDto> getClient(HttpHeaders httpHeaders, int page, int size){
        return clientCouponClient.getCouponClient(httpHeaders, page,size).getBody();

    }


}
