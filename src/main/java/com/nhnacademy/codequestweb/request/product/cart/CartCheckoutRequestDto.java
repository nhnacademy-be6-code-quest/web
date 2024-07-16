package com.nhnacademy.codequestweb.request.product.cart;


import jakarta.validation.constraints.Size;
import java.util.List;

public record CartCheckoutRequestDto(
        @Size(min = 1)
        List<Long> productIdList
){
}
