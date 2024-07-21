package com.nhnacademy.codequestweb.controller.admin;

import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.order.common.OrderResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointPolicyAdminListResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import com.nhnacademy.codequestweb.service.admin.AdminService;
import com.nhnacademy.codequestweb.service.order.AdminOrderService;
import com.nhnacademy.codequestweb.service.point.PointAccumulationService;
import com.nhnacademy.codequestweb.service.point.PointPolicyService;
import com.nhnacademy.codequestweb.service.point.PointUsageService;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;
    @Mock
    private PointAccumulationService pointAccumulationService;
    @Mock
    private PointUsageService pointUsageService;
    @Mock
    private PointPolicyService pointPolicyService;
    @Mock
    private AdminOrderService adminOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdmin() {
        String result = adminController.admin(new MockHttpServletRequest());
        assertEquals("redirect:/admin/client/0", result);
    }

    @Test
    void testAdminClient() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access", "token"));

        Page<ClientPrivacyResponseDto> mockPage = new PageImpl<>(Arrays.asList(new ClientPrivacyResponseDto()));
        when(adminService.privacyList(anyInt(), anyInt(), anyString(), anyBoolean(), anyString())).thenReturn(mockPage);

        String result = adminController.adminClient(0, request);

        assertEquals("index", result);
        verify(adminService).privacyList(0, 4, "clientId", false, "token");
    }

    @Test
    void testUnauthorized() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        FeignException.Unauthorized exception = mock(FeignException.Unauthorized.class);

        String result = adminController.unauthorized(exception, request);
        assertEquals("redirect:/auth", result);

        request.setCookies(new Cookie("access", "token"));
        result = adminController.unauthorized(exception, request);
        assertEquals("redirect:/", result);
    }

    @Test
    void testAdminPageRewardPoint() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access", "token"));

        Page<PointAccumulationAdminPageResponseDto> mockPage = new PageImpl<>(Arrays.asList(new PointAccumulationAdminPageResponseDto()));
        when(pointAccumulationService.userPoint(any(), anyInt(), anyInt())).thenReturn(mockPage);

        String result = adminController.adminPageRewardPoint(request, 0, 10);

        assertEquals("index", result);
        verify(pointAccumulationService).userPoint(any(), eq(0), eq(10));
    }

    @Test
    void testAdminPagUsePoint() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access", "token"));

        Page<PointUsageAdminPageResponseDto> mockPage = new PageImpl<>(Arrays.asList(new PointUsageAdminPageResponseDto()));
        when(pointUsageService.userUsePoint(any(), anyInt(), anyInt())).thenReturn(mockPage);

        String result = adminController.adminPagUsePoint(request, 0, 10);

        assertEquals("index", result);
        verify(pointUsageService).userUsePoint(any(), eq(0), eq(10));
    }

    @Test
    void testPointPolicy() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access", "token"));

        Page<PointPolicyAdminListResponseDto> mockPage = new PageImpl<>(Arrays.asList(new PointPolicyAdminListResponseDto()));
        when(pointPolicyService.findPointPolicies(any(), anyInt(), anyInt())).thenReturn(mockPage);

        String result = adminController.pointPolicy(request, 0, 10);

        assertEquals("index", result);
        verify(pointPolicyService).findPointPolicies(any(), eq(0), eq(10));
    }

    @Test
    void testAdminFindOrder() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access", "token"));

        Page<OrderResponseDto> mockPage = new PageImpl<>(Arrays.asList(new OrderResponseDto()));
        when(adminOrderService.getAllOrder(anyInt(), anyInt(), anyString(), anyString())).thenReturn(mockPage);

        String result = adminController.adminFindOrder(request, 20, 0);

        assertEquals("index", result);
        verify(adminOrderService).getAllOrder(20, 0, "orderDatetime", "desc");
    }

    @Test
    void testAdminShippingPolicy() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access", "token"));

        List<ShippingPolicyGetResponseDto> mockList = Arrays.asList(ShippingPolicyGetResponseDto.builder().build());
        when(adminOrderService.getShippingPolicies(any(HttpServletRequest.class))).thenReturn(mockList);

        Model model = mock(Model.class);

        String result = adminController.adminShippingPolicy(request, model);

        assertEquals("index", result);
        verify(adminOrderService).getShippingPolicies(request);
    }
}