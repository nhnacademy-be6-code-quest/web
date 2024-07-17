package com.nhnacademy.codequestweb.response.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientRoleResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        ClientRoleResponseDto dto = new ClientRoleResponseDto();
        assertNotNull(dto);
        assertNull(dto.getRoles());
    }

    @Test
    void testAllArgsConstructor() {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        ClientRoleResponseDto dto = new ClientRoleResponseDto(roles);

        assertNotNull(dto);
        assertEquals(roles, dto.getRoles());
    }

    @Test
    void getRoles() {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        ClientRoleResponseDto dto = new ClientRoleResponseDto(roles);

        assertEquals(roles, dto.getRoles());
    }

    @Test
    void setRoles() {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        ClientRoleResponseDto dto = new ClientRoleResponseDto();
        dto.setRoles(roles);

        assertEquals(roles, dto.getRoles());
    }

    @Test
    void testBuilder() {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        ClientRoleResponseDto dto = ClientRoleResponseDto.builder()
                .roles(roles)
                .build();

        assertNotNull(dto);
        assertEquals(roles, dto.getRoles());
    }
}
