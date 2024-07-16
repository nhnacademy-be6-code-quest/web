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


}
