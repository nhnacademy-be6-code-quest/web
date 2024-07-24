package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.*;
import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.request.coupon.CouponRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CouponServiceTest {

    @Mock
    private CouponClient couponClient;
    @Mock
    private CouponTypeClient couponTypeClient;
    @Mock
    private UserCouponClient userCouponClient;
    @Mock
    private CouponPolicyClient couponPolicyClient;

    @InjectMocks
    private CouponService couponService;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        headers = new HttpHeaders();
    }

    @Test
    void testSaveCoupon() {
        CouponRegisterRequestDto requestDto = new CouponRegisterRequestDto(1L, 1L, List.of(), LocalDate.now(), Status.USED);
        long couponPolicyId = 1L;

        couponService.saveCoupon(headers, requestDto, couponPolicyId);

        verify(couponClient).saveCoupon(headers, couponPolicyId, requestDto);
    }

    @Test
    void testFindMyPageCoupons() {
        int page = 0;
        int size = 10;
        Status status = Status.AVAILABLE;

        List<CouponMyPageCouponResponseDto> couponList = new ArrayList<>();
        couponList.add(new CouponMyPageCouponResponseDto());
        Page<CouponMyPageCouponResponseDto> mockPage = new PageImpl<>(couponList);

        when(couponClient.findMyPageCoupons(headers, page, size, status))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<CouponMyPageCouponResponseDto> result = couponService.findMyPageCoupons(headers, page, size, status);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(couponClient).findMyPageCoupons(headers, page, size, status);
    }

    @Test
    void testFindUsersCoupons() {
        int page = 0;
        int size = 10;
        Status status = Status.AVAILABLE;

        List<CouponAdminPageCouponResponseDto> couponList = new ArrayList<>();
        couponList.add(new CouponAdminPageCouponResponseDto());
        Page<CouponAdminPageCouponResponseDto> mockPage = new PageImpl<>(couponList);

        when(couponClient.findUserCoupons(headers, page, size, status))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<CouponAdminPageCouponResponseDto> result = couponService.findUsersCoupons(headers, page, size, status);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(couponClient).findUserCoupons(headers, page, size, status);
    }

    @Test
    void testGetClient() {
        int page = 0;
        int size = 10;

        List<ClientCouponPaymentResponseDto> clientList = new ArrayList<>();
        clientList.add(new ClientCouponPaymentResponseDto());
        Page<ClientCouponPaymentResponseDto> mockPage = new PageImpl<>(clientList);

        when(userCouponClient.getCouponPaymentsClient(headers, page, size))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<ClientCouponPaymentResponseDto> result = couponService.getClient(headers, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(userCouponClient).getCouponPaymentsClient(headers, page, size);
    }

    @Test
    void testGetAllCouponTypes() {
        List<CouponTypeResponseDto> typeList = new ArrayList<>();
        typeList.add(new CouponTypeResponseDto());

        when(couponTypeClient.findAllType(headers)).thenReturn(typeList);

        List<CouponTypeResponseDto> result = couponService.getAllCouponTypes(headers);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(couponTypeClient).findAllType(headers);
    }

    @Test
    void testFindCouponType() {
        long couponPolicyId = 1L;
        CouponProvideTypeResponseDto mockResponse = new CouponProvideTypeResponseDto();

        when(couponPolicyClient.findCouponType(headers, couponPolicyId)).thenReturn(mockResponse);

        CouponProvideTypeResponseDto result = couponService.findCouponType(headers, couponPolicyId);

        assertNotNull(result);
        verify(couponPolicyClient).findCouponType(headers, couponPolicyId);
    }
}