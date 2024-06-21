package com.nhnacademy.codequestweb.response.auth;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ROLE_ADMIN, ROLE_USER, ROLE_NON_USER;

    private static final String prefix = "ROLE_";

    @JsonCreator
    public static Role jsonCreator(String role) {
        role = role.toUpperCase();
        if (!role.startsWith(prefix)) {
            role = prefix + role;
        }
        for (Role status : Role.values()) {
            if (status.toString().equals(role)) {
                return status;
            }
        }
        throw new UnknownRoleException("Invalid role: " + role);
    }

    public String getAuthority() {
        return this.toString();
    }
}
