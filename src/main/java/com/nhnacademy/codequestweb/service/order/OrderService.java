package com.nhnacademy.codequestweb.service.order;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.request.order.ClientViewOrderPostRequestDto;
import com.nhnacademy.codequestweb.request.order.client.ClientOrderPostRequestDto;
import com.nhnacademy.codequestweb.response.order.ClientViewOrderPostResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderClient orderClient;

    public ResponseEntity<ClientViewOrderPostResponseDto> viewOrder(ClientViewOrderPostRequestDto orderRequestDto){
        return orderClient.viewOrder(orderRequestDto);
    }

    public ResponseEntity<String> createOrder(ClientOrderPostRequestDto clientOrderPostRequestDto){
        return ResponseEntity.ok("주문이 생성되었습니다");
    }


}
