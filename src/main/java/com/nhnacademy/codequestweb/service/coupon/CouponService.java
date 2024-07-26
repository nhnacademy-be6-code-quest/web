package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.CouponClient;
import com.nhnacademy.codequestweb.client.coupon.CouponPolicyClient;
import com.nhnacademy.codequestweb.client.coupon.CouponTypeClient;
import com.nhnacademy.codequestweb.client.coupon.UserCouponClient;
import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.request.coupon.CouponRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.ClientCouponPaymentResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponAdminPageCouponResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponMyPageCouponResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponProvideTypeResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponRewardMethodRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponTypeResponseDto;
import feign.FeignException;
import feign.FeignException.BadRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponClient couponClient;
    private final CouponTypeClient couponTypeClient;
    private final UserCouponClient userCouponClient;
    private final CouponPolicyClient couponPolicyClient;

    public void saveCoupon(HttpHeaders headers, CouponRegisterRequestDto couponRegisterRequestDto,
        long couponPolicyId) {
        couponClient.saveCoupon(headers, couponPolicyId, couponRegisterRequestDto);
    }

    public Page<CouponMyPageCouponResponseDto> findMyPageCoupons(HttpHeaders headers, int page,
        int size, Status status) {
        return couponClient.findMyPageCoupons(headers, page, size, status).getBody();
    }

    public Page<CouponAdminPageCouponResponseDto> findUsersCoupons(HttpHeaders headers, int page,
        int size, Status status) {
        return couponClient.findUserCoupons(headers, page, size, status).getBody();
    }

    public Page<ClientCouponPaymentResponseDto> getClient(HttpHeaders httpHeaders, int page,
        int size) {
        return userCouponClient.getCouponPaymentsClient(httpHeaders, page, size).getBody();
    }

    public List<CouponTypeResponseDto> getAllCouponTypes(HttpHeaders headers) {
        return couponTypeClient.findAllType(headers);
    }

    public CouponProvideTypeResponseDto findCouponType(HttpHeaders headers, long couponPolicyId) {
        return couponPolicyClient.findCouponType(headers, couponPolicyId);
    }

    public String rewardCoupon(HttpHeaders headers,
        CouponRewardMethodRequestDto couponRewardMethodRequestDto) {
        try {
            return couponClient.rewardUserCoupon(headers,
                couponRewardMethodRequestDto.getMethodName()).getBody();
        } catch (FeignException e) {
            if (e.status() == 404 && e.getMessage().contains("회원만 쿠폰을 받을수 있습니다.")) {
                return "회원만 쿠폰을 받을수 있습니다.";
            }
        }
        return "이미 지급받은 쿠폰입니다";
    }
}


