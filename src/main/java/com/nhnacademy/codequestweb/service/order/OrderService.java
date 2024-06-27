package com.nhnacademy.codequestweb.service.order;


import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.order.OrderReviewClient;
import com.nhnacademy.codequestweb.request.order.ClientViewOrderPostRequestDto;
import com.nhnacademy.codequestweb.request.order.client.ClientOrderPostRequestDto;
import com.nhnacademy.codequestweb.response.order.client.ClientViewOrderPostResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class OrderService {

    private OrderClient orderClient;
    private OrderReviewClient orderReviewClient;

    public ResponseEntity<ClientViewOrderPostResponseDto> viewOrder(ClientViewOrderPostRequestDto orderRequestDto){
        return orderClient.viewOrder(orderRequestDto);
    }

    public long createOrder(ClientOrderPostRequestDto clientOrderPostRequestDto){
        return orderClient.createOrder(clientOrderPostRequestDto).getBody().orderId();
    }

// 이걸 거치면(feign) cors 방지!! cors란? 호출하는 ip와 호출되는 ip가 다를 때 브라우저 단에서 보안적으로 호출을 막는것!
// service 계층에서는 필요한 dto를 (필요에 따라 가공?) 반환한다!! 반환 값이 http status code를 가지고 있으니깐, 에러 나도 알 길이 없어!!!!

    public ResponseEntity<String> getOrderStatus(Long orderDetailId){
        return orderReviewClient.getOrderStatus(orderDetailId);
    }

}