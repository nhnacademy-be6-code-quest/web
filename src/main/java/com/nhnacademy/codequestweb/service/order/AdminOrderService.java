package com.nhnacademy.codequestweb.service.order;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.shipping.ShippingPolicyClient;
import com.nhnacademy.codequestweb.response.order.common.OrderResponseDto;
import com.nhnacademy.codequestweb.request.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminOrderService {

    private final OrderClient orderClient;
    private final ShippingPolicyClient shippingPolicyClient;

    public Page<OrderResponseDto> getAllOrder(int pageSize, int pageNo, String sortBy, String sortDir){
        return orderClient.getOrder(pageSize, pageNo, sortBy, sortDir).getBody();
    }

    public List<ShippingPolicyGetResponseDto> getShippingPolicies(HttpServletRequest req){
        HttpHeaders headers = getHeader(req);
        return shippingPolicyClient.getAllShippingPolicies(headers).getBody();
    }

    public void updateShippingPolicy(HttpServletRequest req, AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto){
        HttpHeaders headers = getHeader(req);
        shippingPolicyClient.updateShippingPolicy(headers, adminShippingPolicyPutRequestDto);
    }

    public void updateOrderStatus(HttpServletRequest req, long orderId, String korStatus){
        HttpHeaders headers = getHeader(req);
        orderClient.updateOrderStatus(headers, orderId, korStatus);
    }

    private HttpHeaders getHeader(HttpServletRequest req){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        return headers;
    }


}
