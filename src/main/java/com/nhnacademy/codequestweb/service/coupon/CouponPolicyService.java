package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.BookCouponClient;
import com.nhnacademy.codequestweb.client.coupon.CouponPolicyClient;
import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyListResponseDto;
import com.nhnacademy.codequestweb.response.coupon.ProductGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponPolicyService {


    private final CouponPolicyClient couponPolicyClient;

    private final BookCouponClient bookCouponClient;


    public Page<ProductGetResponseDto> getAllBooks(HttpHeaders headers, Integer page, String sort,
        Boolean desc) {
        return bookCouponClient.getAllProducts(headers, page, sort, desc).getBody();
    }




    public Page<CouponPolicyListResponseDto> getAllCouponPolicies(HttpHeaders headers,int page, int size) {

        return couponPolicyClient.getAllCouponPolices(headers, page, size).getBody();
    }

    public void savePolicy(HttpHeaders headers, CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto) {
        couponPolicyClient.savePolicy(headers, couponPolicyRegisterRequestDto);
    }

}
