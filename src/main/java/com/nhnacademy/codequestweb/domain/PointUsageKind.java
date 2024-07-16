package com.nhnacademy.codequestweb.domain;

public enum PointUsageKind {
    PAYMENT(0, "결재"), REFUND(1, "환불");

    private final int code;
    private final String value;

    PointUsageKind(int code, String value) {
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


