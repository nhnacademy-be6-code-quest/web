package com.nhnacademy.codequestweb.service.admin;

import com.nhnacademy.codequestweb.client.admin.AdminClient;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {

    @Mock
    private AdminClient adminClient;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPrivacyList() {
        // Arrange
        int page = 0;
        int size = 10;
        String sort = "id";
        boolean desc = true;
        String access = "all";

        ClientPrivacyResponseDto mockDto = new ClientPrivacyResponseDto();
        Page<ClientPrivacyResponseDto> mockPage = new PageImpl<>(Collections.singletonList(mockDto));
        ResponseEntity<Page<ClientPrivacyResponseDto>> mockResponse = ResponseEntity.ok(mockPage);

        when(adminClient.privacyPage(page, size, sort, desc, access)).thenReturn(mockResponse);

        // Act
        Page<ClientPrivacyResponseDto> result = adminService.privacyList(page, size, sort, desc, access);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        verify(adminClient, times(1)).privacyPage(page, size, sort, desc, access);
    }
}