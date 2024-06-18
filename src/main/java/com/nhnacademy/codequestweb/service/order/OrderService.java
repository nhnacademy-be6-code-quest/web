package com.nhnacademy.codequestweb.service.order;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.request.order.OrderRequestDto;
import com.nhnacademy.codequestweb.response.order.OrderResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderClient orderClient;

    public ResponseEntity<OrderResponseDto> gotoOrder(OrderRequestDto orderRequestDto){
        return orderClient.tryOrder(orderRequestDto);
    }
}
