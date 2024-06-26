package com.nhnacademy.codequestweb.response.order.admin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nhnacademy.codequestweb.deserializer.DateToLocalDateTimeDeserializer;
import com.nhnacademy.codequestweb.response.order.field.OrderedProductDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
// 관리자가 보는,, 회원이 주문한 주문 내역 건
public record AdminAllOrdersGetResponseDto(
        long clientId, // 회원 아이디
        String clientEmail, // 회원 이메일
        @JsonDeserialize(using = DateToLocalDateTimeDeserializer.class)
        LocalDateTime orderDate, // 주문날짜
        List<OrderedProductDto> orderedProductDtoList // 주문한 상품정보들
)
{

}