package com.nhnacademy.codequestweb.domain;

public enum CouponKind {
    WELCOME(0,"회원가입"), BIRTHDAY(1, "생일"), BOOK(2, "상품"), CATEGORY(3, "상품 카테고리"), DISCOUNT(4, "금액 할인");
    private final int code;
    private final String value;

    CouponKind(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static CouponKind fromCode(int code) {
        for (CouponKind type : CouponKind.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType code: " + code);
    }

    public static CouponKind fromValue(String value) {
        for (CouponKind type : CouponKind.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountType value: " + value);
    }
}
