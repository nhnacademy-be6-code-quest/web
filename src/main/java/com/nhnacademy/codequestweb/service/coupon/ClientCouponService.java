package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.ClientCouponClient;
import com.nhnacademy.codequestweb.client.product.bookProduct.BookProductClient;
import com.nhnacademy.codequestweb.response.coupon.ClientCouponPaymentResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
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

    @Autowired
    private BookProductClient bookProductClient;


    public Page<BookProductGetResponseDto> getAllBookPage(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc
    ){
        return bookProductClient.getAllBookPage(page,sort,desc).getBody();


    }

    public Page<ClientCouponPaymentResponseDto> getClient(HttpHeaders httpHeaders, int page, int size){
        return clientCouponClient.getCouponClient(httpHeaders, page,size).getBody();

    }
}
