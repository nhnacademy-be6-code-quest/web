package com.nhnacademy.codequestweb.request.order.field;

import jakarta.validation.constraints.NotNull;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemDto{
    @NotNull
    Long productId;
    @NotNull
    Long quantity;
    List<Long> categoryIdList;
    boolean packable;
}