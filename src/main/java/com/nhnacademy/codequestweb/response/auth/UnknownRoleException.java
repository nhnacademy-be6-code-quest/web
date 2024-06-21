package com.nhnacademy.codequestweb.response.auth;

public class UnknownRoleException extends RuntimeException {
    public UnknownRoleException(String message) {
        super(message);
    }
}
