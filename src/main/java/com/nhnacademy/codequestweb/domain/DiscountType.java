package com.nhnacademy.codequestweb.domain;

public enum DiscountType {
    AMOUNTDISCOUNT(0, "금액 할인"),
    PERCENTAGEDISCOUNT(1, "퍼센트 할인");

    private final int code;
    private final String value;

    DiscountType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}

