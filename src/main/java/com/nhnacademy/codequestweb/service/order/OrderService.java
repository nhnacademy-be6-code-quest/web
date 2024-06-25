package com.nhnacademy.codequestweb.service.order;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.order.OrderReviewClient;
import com.nhnacademy.codequestweb.request.order.ClientOrderPostRequestDto;
import com.nhnacademy.codequestweb.response.order.ClientOrderPostResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderClient orderClient;
    private OrderReviewClient orderReviewClient;

    public ResponseEntity<ClientOrderPostResponseDto> gotoOrder(ClientOrderPostRequestDto orderRequestDto){
        return orderClient.tryOrder(orderRequestDto);
    }

    public ResponseEntity<String> getOrderStatus(Long orderDetailId){
        return orderReviewClient.getOrderStatus(orderDetailId);
    }

}