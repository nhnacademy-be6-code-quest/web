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

    public static Status fromCode(int code) {
        for (Status type : Status.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType code: " + code);
    }

    public static Status fromValue(String value) {
        for (Status type : Status.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType value: " + value);
    }
}
