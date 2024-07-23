package com.nhnacademy.codequestweb.exception.product;

import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;

public class CartProcessingException extends RuntimeException {
    public CartProcessingException(String message) {
        super(message);
    }
}
