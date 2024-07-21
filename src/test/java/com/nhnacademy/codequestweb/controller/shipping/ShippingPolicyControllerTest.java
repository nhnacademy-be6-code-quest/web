package com.nhnacademy.codequestweb.controller.shipping;

import com.nhnacademy.codequestweb.request.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import com.nhnacademy.codequestweb.service.order.AdminOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ShippingPolicyControllerTest {

    @Mock
    private AdminOrderService adminOrderService;

    @InjectMocks
    private ShippingPolicyController shippingPolicyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateShippingPolicy() {
        // 준비
        MockHttpServletRequest request = new MockHttpServletRequest();
        AdminShippingPolicyPutRequestDto requestDto = AdminShippingPolicyPutRequestDto.builder().build();
        List<ShippingPolicyGetResponseDto> mockPolicies = Arrays.asList(
                ShippingPolicyGetResponseDto.builder().build(),
                ShippingPolicyGetResponseDto.builder().build()
        );

        when(adminOrderService.getShippingPolicies(request)).thenReturn(mockPolicies);

        // 실행
        String viewName = shippingPolicyController.updateShippingPolicy(request, requestDto);

        // 검증
        verify(adminOrderService).updateShippingPolicy(request, requestDto);
        verify(adminOrderService).getShippingPolicies(request);

        assertEquals("index", viewName);
        assertEquals("adminPage", request.getAttribute("view"));
        assertEquals("adminShippingPolicy", request.getAttribute("adminPage"));
        assertEquals("order", request.getAttribute("activeSection"));
        assertEquals(mockPolicies, request.getAttribute("shippingPolicyList"));
    }

    @Test
    void testUpdateShippingPolicyWithException() {
        // 준비
        MockHttpServletRequest request = new MockHttpServletRequest();
        AdminShippingPolicyPutRequestDto requestDto = AdminShippingPolicyPutRequestDto.builder().build();

        doThrow(new RuntimeException("Update failed")).when(adminOrderService).updateShippingPolicy(request, requestDto);

        // 실행 및 예외 검증
        assertThrows(RuntimeException.class, () -> {
            shippingPolicyController.updateShippingPolicy(request, requestDto);
        });

        // 검증
        verify(adminOrderService).updateShippingPolicy(request, requestDto);
        verify(adminOrderService, never()).getShippingPolicies(request);
    }
}