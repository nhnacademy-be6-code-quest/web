package com.nhnacademy.codequestweb.request.order;

import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
public record ClientViewOrderPostRequestDto ( // 회원이 주문 시도했을 때 필요한 dtㅐ
    List<OrderItemDto> orderItemDtoList // 주문 목록
)
{

}

