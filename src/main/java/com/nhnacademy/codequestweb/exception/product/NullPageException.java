package com.nhnacademy.codequestweb.exception.product;

public class NullPageException extends RuntimeException {
    public NullPageException() {
        super("The book page is null");
    }
}
