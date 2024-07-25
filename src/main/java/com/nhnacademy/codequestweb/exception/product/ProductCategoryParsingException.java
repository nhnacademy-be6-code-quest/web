package com.nhnacademy.codequestweb.exception.product;

public class ProductCategoryParsingException extends RuntimeException {
    public ProductCategoryParsingException(String message) {
        super("JsonProcessingException occurred while parsing ProductCategory Object. message : "+ message);
    }
}
