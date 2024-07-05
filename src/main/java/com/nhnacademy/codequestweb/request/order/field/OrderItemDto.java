package com.nhnacademy.codequestweb.request.order.field;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class OrderItemDto {
//    private Long productId;
//    private Long quantity;
//    private List<Long> categoryId;
//}
public record OrderItemDto (
        Long productId,
        Long quantity,
        List<Long> categoryId
){

}