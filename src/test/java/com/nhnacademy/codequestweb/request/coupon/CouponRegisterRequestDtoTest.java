package com.nhnacademy.codequestweb.request.coupon;


import com.nhnacademy.codequestweb.domain.Status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

class CouponRegisterRequestDtoTest {
    private final Validator validator;
    public CouponRegisterRequestDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void testValidCouponRequestDto() {
        CouponRegisterRequestDto dto = new CouponRegisterRequestDto(
                1L,
                2L,
                List.of(1L, 2L, 3L),
                LocalDate.of(2023, 12, 31),
                Status.AVAILABLE
        );
        Set<ConstraintViolation<CouponRegisterRequestDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }
    @Test
    void testInvalidCouponRequestDto() {
        CouponRegisterRequestDto dto = new CouponRegisterRequestDto(
                1L,
                2L,
                null,  // clientId is null
                null,  // expirationDate is null
                null   // status is null
        );
        Set<ConstraintViolation<CouponRegisterRequestDto>> violations = validator.validate(dto);
        assertEquals(3, violations.size());
    }
    @Test
    void testFieldValues() {
        long couponTypeId = 1L;
        long couponPolicyId = 2L;
        List<Long> clientId = List.of(1L, 2L, 3L);
        LocalDate expirationDate = LocalDate.of(2023, 12, 31);
        Status status = Status.UNAVAILABLE;
        CouponRegisterRequestDto dto = new CouponRegisterRequestDto(
                couponTypeId,
                couponPolicyId,
                clientId,
                expirationDate,
                status
        );
        assertEquals(couponTypeId, dto.couponTypeId());
        assertEquals(couponPolicyId, dto.couponPolicyId());
        assertEquals(clientId, dto.clientId());
        assertEquals(expirationDate, dto.expirationDate());
        assertEquals(status, dto.status());
    }
}