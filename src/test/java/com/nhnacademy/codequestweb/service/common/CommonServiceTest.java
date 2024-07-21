package com.nhnacademy.codequestweb.service.common;

import com.nhnacademy.codequestweb.client.common.CommonClient;
import com.nhnacademy.codequestweb.response.common.ClientRoleResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CommonServiceTest {

    @Mock
    private CommonClient commonClient;

    @InjectMocks
    private CommonService commonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsAdminWithAdminRole() {
        String accessToken = "admin-token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("access", accessToken);

        ClientRoleResponseDto responseDto = new ClientRoleResponseDto();
        responseDto.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));

        when(commonClient.getClientRole(headers)).thenReturn(ResponseEntity.ok(responseDto));

        boolean result = commonService.isAdmin(accessToken);

        assertTrue(result);
        verify(commonClient, times(1)).getClientRole(headers);
    }

    @Test
    void testIsAdminWithoutAdminRole() {
        String accessToken = "user-token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("access", accessToken);

        ClientRoleResponseDto responseDto = new ClientRoleResponseDto();
        responseDto.setRoles(Collections.singletonList("ROLE_USER"));

        when(commonClient.getClientRole(headers)).thenReturn(ResponseEntity.ok(responseDto));

        boolean result = commonService.isAdmin(accessToken);

        assertFalse(result);
        verify(commonClient, times(1)).getClientRole(headers);
    }

    @Test
    void testIsAdminWithEmptyRoles() {
        String accessToken = "empty-token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("access", accessToken);

        ClientRoleResponseDto responseDto = new ClientRoleResponseDto();
        responseDto.setRoles(Collections.emptyList());

        when(commonClient.getClientRole(headers)).thenReturn(ResponseEntity.ok(responseDto));

        boolean result = commonService.isAdmin(accessToken);

        assertFalse(result);
        verify(commonClient, times(1)).getClientRole(headers);
    }
}