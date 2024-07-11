package com.nhnacademy.codequestweb.service.point;

import com.nhnacademy.codequestweb.client.point.OrderPointClient;
import com.nhnacademy.codequestweb.request.point.PointRewardOrderRequestDto;
import com.nhnacademy.codequestweb.request.point.PointUsagePaymentRequestDto;
import com.nhnacademy.codequestweb.response.point.TotalPointAmountResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPointService {

    private final OrderPointClient orderPointClient;


    public TotalPointAmountResponseDto userPoint(HttpHeaders headers){

        return orderPointClient.findPoint(headers);
    }

    public void usePaymentPoint(HttpHeaders headers, PointUsagePaymentRequestDto pointUsagePaymentRequestDto){
        Integer i = 1;
        pointUsagePaymentRequestDto.setPointUsagePayment(i);
        orderPointClient.usePaymentPoint(pointUsagePaymentRequestDto, headers);
    }

    public void rewardPaymentPoint(HttpHeaders headers, PointRewardOrderRequestDto pointRewardOrderRequestDto){
        Integer i= 1;
        pointRewardOrderRequestDto.setAccumulatedPoint(i);
        orderPointClient.rewardOrderPoint(headers, pointRewardOrderRequestDto);
    }


}
