package com.nhnacademy.codequestweb.request.order.field;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record OrderItemDto (
        Long productId,
        Long quantity,
        List<Long> categoryId,
        boolean packable
){

}