package com.nhnacademy.codequestweb.domain;

public enum PointStatus {
   ACTIVATE(0,"활성"),DISABLED(1,"비활성화");

    private final int code;
    private final String value;

    PointStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static PointStatus fromCode(int code) {
        for (PointStatus type : PointStatus.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType code: " + code);
    }

    public static PointStatus fromValue(String value) {
        for (PointStatus type : PointStatus.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType value: " + value);
    }
}
