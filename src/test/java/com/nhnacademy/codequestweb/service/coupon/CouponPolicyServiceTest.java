package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.BookCouponClient;
import com.nhnacademy.codequestweb.client.coupon.CouponPolicyClient;
import com.nhnacademy.codequestweb.domain.DiscountType;
import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyListResponseDto;
import com.nhnacademy.codequestweb.response.coupon.ProductGetResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CouponPolicyServiceTest {

    @Mock
    private CouponPolicyClient couponPolicyClient;

    @Mock
    private BookCouponClient bookCouponClient;

    @InjectMocks
    private CouponPolicyService couponPolicyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        HttpHeaders headers = new HttpHeaders();
        Integer page = 0;
        String sort = "title";
        Boolean desc = false;

        List<ProductGetResponseDto> productList = new ArrayList<>();
        productList.add(new ProductGetResponseDto(1L, "", 1, 1L, 1L, ""));
        Page<ProductGetResponseDto> mockPage = new PageImpl<>(productList);

        when(bookCouponClient.getAllProducts(headers, page, sort, desc))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<ProductGetResponseDto> result = couponPolicyService.getAllBooks(headers, page, sort, desc);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(bookCouponClient).getAllProducts(headers, page, sort, desc);
    }

    @Test
    void testGetAllCouponPolicies() {
        HttpHeaders headers = new HttpHeaders();
        int page = 0;
        int size = 10;

        List<CouponPolicyListResponseDto> policyList = new ArrayList<>();
        policyList.add(new CouponPolicyListResponseDto(/* 필요한 파라미터 */));
        Page<CouponPolicyListResponseDto> mockPage = new PageImpl<>(policyList);

        when(couponPolicyClient.getAllCouponPolices(headers, page, size))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<CouponPolicyListResponseDto> result = couponPolicyService.getAllCouponPolicies(headers, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(couponPolicyClient).getAllCouponPolices(headers, page, size);
    }

    @Test
    void testSavePolicy() {
        HttpHeaders headers = new HttpHeaders();
        CouponPolicyRegisterRequestDto requestDto = new CouponPolicyRegisterRequestDto("", DiscountType.AMOUNTDISCOUNT, 1L, 1L, 1L, 1L, "");

        couponPolicyService.savePolicy(headers, requestDto);

        verify(couponPolicyClient).savePolicy(headers, requestDto);
    }
}