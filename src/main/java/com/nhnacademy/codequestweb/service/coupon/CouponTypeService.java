package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.CouponTypeClient;
import com.nhnacademy.codequestweb.response.auth.coupon.CouponTypeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CouponTypeService {
    @Autowired
    private CouponTypeClient couponTypeClient;
    public List<CouponTypeResponseDto> getAllCouponTypes(){
        return couponTypeClient.findAllType();
    }
}
