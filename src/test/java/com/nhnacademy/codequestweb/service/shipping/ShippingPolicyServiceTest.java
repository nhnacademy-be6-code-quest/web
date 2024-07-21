package com.nhnacademy.codequestweb.service.shipping;

import com.nhnacademy.codequestweb.client.shipping.ShippingPolicyClient;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ShippingPolicyServiceTest {

    @Mock
    private ShippingPolicyClient shippingPolicyClient;

    @InjectMocks
    private ShippingPolicyService shippingPolicyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetShippingPolicy() {
        // 준비
        HttpHeaders headers = new HttpHeaders();
        String type = "standard";
        ShippingPolicyGetResponseDto mockResponse = new ShippingPolicyGetResponseDto(type, 5000, 5000, type);

        when(shippingPolicyClient.getShippingPolicy(headers, type))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // 실행
        ShippingPolicyGetResponseDto result = shippingPolicyService.getShippingPolicy(headers, type);

        // 검증
        assertNotNull(result);
        verify(shippingPolicyClient, times(1)).getShippingPolicy(headers, type);
    }

    @Test
    void testGetShippingPolicyWithNullResponse() {
        // 준비
        HttpHeaders headers = new HttpHeaders();
        String type = "express";

        when(shippingPolicyClient.getShippingPolicy(headers, type))
                .thenReturn(ResponseEntity.ok(null));

        // 실행
        ShippingPolicyGetResponseDto result = shippingPolicyService.getShippingPolicy(headers, type);

        // 검증
        assertNull(result);

        verify(shippingPolicyClient, times(1)).getShippingPolicy(headers, type);
    }

    @Test
    void testGetShippingPolicyWithException() {
        // 준비
        HttpHeaders headers = new HttpHeaders();
        String type = "invalid";

        when(shippingPolicyClient.getShippingPolicy(headers, type))
                .thenThrow(new RuntimeException("API Error"));

        // 실행 및 검증
        assertThrows(RuntimeException.class, () -> shippingPolicyService.getShippingPolicy(headers, type));

        verify(shippingPolicyClient, times(1)).getShippingPolicy(headers, type);
    }
}