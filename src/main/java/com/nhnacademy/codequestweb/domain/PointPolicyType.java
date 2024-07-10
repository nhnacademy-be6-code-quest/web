package com.nhnacademy.codequestweb.domain;

public enum PointPolicyType {
  REVIEW(0,"리뷰"),MEMBERSHIP(1,"회원가입"),REFUND(2,"환불"),PAYMENT(3,"결제");

    private final int code;
    private final String value;

    PointPolicyType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static PointPolicyType fromCode(int code) {
        for (PointPolicyType type : PointPolicyType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType code: " + code);
    }

    public static PointPolicyType fromValue(String value) {
        for (PointPolicyType type : PointPolicyType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType value: " + value);
    }
}
