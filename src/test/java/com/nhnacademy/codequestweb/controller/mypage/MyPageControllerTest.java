package com.nhnacademy.codequestweb.controller.mypage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.nhnacademy.codequestweb.service.mypage.MyPageService;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.service.point.PointAccumulationService;
import com.nhnacademy.codequestweb.service.point.PointUsageService;
import com.nhnacademy.codequestweb.response.mypage.*;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderGetResponseDto;
import com.nhnacademy.codequestweb.response.point.*;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import com.nhnacademy.codequestweb.request.mypage.*;

import jakarta.servlet.http.Cookie;
import feign.FeignException;

import java.util.ArrayList;
import java.util.List;

class MyPageControllerTest {

    @InjectMocks
    private MyPageController myPageController;

    @Mock
    private MyPageService myPageService;

    @Mock
    private PointUsageService pointUsageService;

    @Mock
    private PointAccumulationService pointAccumulationService;

    @Mock
    private OrderService orderService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setCookies(new Cookie("access", "test-access-token"));
    }

    @Test
    void testMypageMain() {
        ClientPrivacyResponseDto mockResponse = new ClientPrivacyResponseDto();
        when(myPageService.getPrivacy(any(HttpHeaders.class))).thenReturn(ResponseEntity.ok(mockResponse));

        String viewName = myPageController.mypageMain(request, response);

        assertEquals("index", viewName);
        assertEquals("mypage", request.getAttribute("view"));
        assertEquals("profile", request.getAttribute("mypage"));
        assertEquals("client", request.getAttribute("activeSection"));
        assertNotNull(request.getAttribute("profile"));

        verify(myPageService).getPrivacy(any(HttpHeaders.class));
    }

    @Test
    void testMypageDelivery() {
        List<ClientDeliveryAddressResponseDto> mockAddresses = new ArrayList<>();
        when(myPageService.getDeliveryAddresses(any(HttpHeaders.class))).thenReturn(ResponseEntity.ok(mockAddresses));

        String viewName = myPageController.mypageDelivery(request);

        assertEquals("index", viewName);
        assertEquals("mypage", request.getAttribute("view"));
        assertEquals("delivaryaddress", request.getAttribute("mypage"));
        assertEquals("client", request.getAttribute("activeSection"));
        assertNotNull(request.getAttribute("clientDeliveryAddresses"));

        verify(myPageService).getDeliveryAddresses(any(HttpHeaders.class));
    }

    @Test
    void testRegisterDeliveryAddress() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        when(myPageService.registerAddress(any(HttpHeaders.class), any(ClientRegisterAddressRequestDto.class)))
                .thenReturn(ResponseEntity.ok("Success"));

        String result = myPageController.registerDeliveryAddress(dto, request);

        assertEquals("redirect:/mypage/delivary", result);

        verify(myPageService).registerAddress(any(HttpHeaders.class), any(ClientRegisterAddressRequestDto.class));
    }

    @Test
    void testRegisterDeliveryAddressError() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        when(myPageService.registerAddress(any(HttpHeaders.class), any(ClientRegisterAddressRequestDto.class)))
                .thenThrow(FeignException.class);

        String result = myPageController.registerDeliveryAddress(dto, request);

        assertTrue(result.startsWith("redirect:/mypage/delivary?alterMessage="));

        verify(myPageService).registerAddress(any(HttpHeaders.class), any(ClientRegisterAddressRequestDto.class));
    }

    @Test
    void testDeleteDeliveryAddress() {
        when(myPageService.deleteDeliveryAddress(any(HttpHeaders.class), anyLong()))
                .thenReturn(ResponseEntity.ok("Success"));

        ResponseEntity<String> result = myPageController.deleteDeliveryAddress(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Successfully deleted delivery address", result.getBody());

        verify(myPageService).deleteDeliveryAddress(any(HttpHeaders.class), eq(1L));
    }

    @Test
    void testWithdrawalGet() {
        String viewName = myPageController.withdrawal(request);

        assertEquals("index", viewName);
        assertEquals("mypage", request.getAttribute("view"));
        assertEquals("withdrawal", request.getAttribute("mypage"));
        assertEquals("client", request.getAttribute("activeSection"));
    }

    @Test
    void testWithdrawalDelete() {
        when(myPageService.deleteClient(any(HttpHeaders.class)))
                .thenReturn(ResponseEntity.ok("Success"));

        ResponseEntity<String> result = myPageController.withdrawal("password", request);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        verify(myPageService).deleteClient(any(HttpHeaders.class));
    }

    @Test
    void testWithdrawalDeleteError() {
        when(myPageService.deleteClient(any(HttpHeaders.class)))
                .thenThrow(FeignException.Unauthorized.class);

        ResponseEntity<String> result = myPageController.withdrawal("password", request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

        verify(myPageService).deleteClient(any(HttpHeaders.class));
    }

    @Test
    void testPhoneGet() {
        List<ClientPhoneNumberResponseDto> mockPhones = new ArrayList<>();
        when(myPageService.getPhoneNumbers(any(HttpHeaders.class))).thenReturn(ResponseEntity.ok(mockPhones));

        String viewName = myPageController.phone(request);

        assertEquals("index", viewName);
        assertEquals("mypage", request.getAttribute("view"));
        assertEquals("phoneNumber", request.getAttribute("mypage"));
        assertEquals("client", request.getAttribute("activeSection"));
        assertNotNull(request.getAttribute("clientPhoneNumber"));

        verify(myPageService).getPhoneNumbers(any(HttpHeaders.class));
    }

    @Test
    void testAddPhone() {
        ClientRegisterPhoneNumberRequestDto dto = new ClientRegisterPhoneNumberRequestDto();
        when(myPageService.registerPhoneNumbers(any(HttpHeaders.class), any(ClientRegisterPhoneNumberRequestDto.class)))
                .thenReturn(ResponseEntity.ok("Success"));

        String result = myPageController.addPhone(request, dto);

        assertEquals("redirect:/mypage/phone", result);

        verify(myPageService).registerPhoneNumbers(any(HttpHeaders.class), any(ClientRegisterPhoneNumberRequestDto.class));
    }

    @Test
    void testDeletePhone() {
        when(myPageService.deletePhoneNumber(any(HttpHeaders.class), anyLong()))
                .thenReturn(ResponseEntity.ok("Success"));

        ResponseEntity<String> result = myPageController.deletePhone(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Successfully deleted phone number", result.getBody());

        verify(myPageService).deletePhoneNumber(any(HttpHeaders.class), eq(1L));
    }

    @Test
    void testUpdateProfile() {
        ClientUpdatePrivacyRequestDto dto = new ClientUpdatePrivacyRequestDto();
        when(myPageService.updateClient(any(HttpHeaders.class), any(ClientUpdatePrivacyRequestDto.class)))
                .thenReturn(ResponseEntity.ok("Success"));

        String result = myPageController.updateProfile(dto, request);

        assertEquals("redirect:/mypage", result);

        verify(myPageService).updateClient(any(HttpHeaders.class), any(ClientUpdatePrivacyRequestDto.class));
    }

    @Test
    void testMyPageRewardPoint() {
        Page<PointAccumulationMyPageResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(pointAccumulationService.clientPoint(any(HttpHeaders.class), anyInt(), anyInt())).thenReturn(mockPage);

        String viewName = myPageController.myPageRewardPoint(request, 0, 100);

        assertEquals("index", viewName);
        assertEquals("mypage", request.getAttribute("view"));
        assertEquals("pointReward", request.getAttribute("mypage"));
        assertEquals("coupon", request.getAttribute("activeSection"));
        assertNotNull(request.getAttribute("points"));

        verify(pointAccumulationService).clientPoint(any(HttpHeaders.class), eq(0), eq(100));
    }

    @Test
    void testMyPageUsedPoint() {
        Page<PointUsageMyPageResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(pointUsageService.clientUsePoint(any(HttpHeaders.class), anyInt(), anyInt())).thenReturn(mockPage);

        String viewName = myPageController.myPageUsedPoint(request, 0, 10);

        assertEquals("index", viewName);
        assertEquals("mypage", request.getAttribute("view"));
        assertEquals("pointUsed", request.getAttribute("mypage"));
        assertEquals("point", request.getAttribute("activeSection"));
        assertNotNull(request.getAttribute("points"));

        verify(pointUsageService).clientUsePoint(any(HttpHeaders.class), eq(0), eq(10));
    }

    @Test
    void testMypageOrders() {
        Page<ClientOrderGetResponseDto> mockPage = new PageImpl<>(new ArrayList<>());

        when(orderService.getClientOrders(any(HttpHeaders.class), anyInt(), anyInt(), eq("order.orderDatetime"), eq("desc")))
                .thenReturn(mockPage);


        String viewName = myPageController.mypageOrders(request, 10, 0);

        assertEquals("index", viewName);
        assertEquals("mypage", request.getAttribute("view"));
        assertEquals("orders", request.getAttribute("mypage"));
        assertEquals("order", request.getAttribute("activeSection"));
        assertNotNull(request.getAttribute("orders"));

        verify(orderService).getClientOrders(any(HttpHeaders.class), eq(10), eq(0), eq("order.orderDatetime"), eq("desc"));
    }

    @Test
    void testMypageReview() {
        Page<ReviewInfoResponseDto> mockPage = new PageImpl<>(new ArrayList<>());
        when(myPageService.getMyReviewInfo(anyString(), anyInt(), eq(5))).thenReturn(mockPage);

        String viewName = myPageController.mypageReview(request, 0);

        assertEquals("index", viewName);
        assertEquals("mypage", request.getAttribute("view"));
        assertEquals("review", request.getAttribute("mypage"));
        assertEquals("review", request.getAttribute("activeSection"));
        assertNotNull(request.getAttribute("reviews"));

        verify(myPageService).getMyReviewInfo(anyString(), eq(0), eq(5));
    }

    @Test
    void testNotFound() {
        String result = myPageController.notFound(request, new Exception());

        assertEquals("redirect:/auth", result);
    }
}