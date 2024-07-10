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

    public static PointUsageKind fromCode(int code) {
        for (PointUsageKind type : PointUsageKind.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType code: " + code);
    }

    public static PointUsageKind fromValue(String value) {
        for (PointUsageKind type : PointUsageKind.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType value: " + value);
    }
}


