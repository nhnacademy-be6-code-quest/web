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

    public static DiscountType fromCode(int code) {
        for (DiscountType type : DiscountType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType code: " + code);
    }

    public static DiscountType fromValue(String value) {
        for (DiscountType type : DiscountType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType value: " + value);
    }
}

