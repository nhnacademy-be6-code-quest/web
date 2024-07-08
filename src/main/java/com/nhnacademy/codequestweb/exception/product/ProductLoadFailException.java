package com.nhnacademy.codequestweb.exception.product;

public class ProductLoadFailException extends RuntimeException {
    public ProductLoadFailException(String message) {
        super(message);
    }
}
