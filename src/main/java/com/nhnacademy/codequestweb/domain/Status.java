package com.nhnacademy.codequestweb.domain;

public enum Status {
    USED(0, "사용완료"),
    AVAILABLE(1, "사용 가능"),
    UNAVAILABLE(2, "사용 불가");

    private final int code;
    private final String value;

    Status(int code, String value) {
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
