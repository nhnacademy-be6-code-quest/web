package com.nhnacademy.codequestweb.request.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.codequestweb.domain.DiscountType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CouponPolicyRegisterRequestDtoTest {
    private final Validator validator;
    public CouponPolicyRegisterRequestDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void testValidCouponPolicyRegisterRequestDto() {
        CouponPolicyRegisterRequestDto dto = new CouponPolicyRegisterRequestDto(
                "Birthday Discount",
                DiscountType.AMOUNTDISCOUNT,
                1000L,
                5000L,
                2000L,
                1L,
                "BIRTHDAY"
        );
        Set<ConstraintViolation<CouponPolicyRegisterRequestDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }
    @Test
    void testInvalidCouponPolicyRegisterRequestDto() {
        CouponPolicyRegisterRequestDto dto = new CouponPolicyRegisterRequestDto(
                null,
                DiscountType.AMOUNTDISCOUNT,
                1L,
                -1L,
                -1L,
                -1L,
                "BIRTHDAY"
        );
        Set<ConstraintViolation<CouponPolicyRegisterRequestDto>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }
    @Test
    void testFieldValues() {
        String description = "Birthday Discount";
        DiscountType discountType = DiscountType.AMOUNTDISCOUNT;
        long discountValue = 1000L;
        long minPurchaseAmount = 5000L;
        long maxDiscountAmount = 2000L;
        Long id = 1L;
        String typeName = "BIRTHDAY";
        CouponPolicyRegisterRequestDto dto = new CouponPolicyRegisterRequestDto(
                description,
                discountType,
                discountValue,
                minPurchaseAmount,
                maxDiscountAmount,
                id,
                typeName
        );
        assertEquals(description, dto.couponPolicyDescription());
        assertEquals(discountType, dto.discountType());
        assertEquals(discountValue, dto.discountValue());
        assertEquals(minPurchaseAmount, dto.minPurchaseAmount());
        assertEquals(maxDiscountAmount, dto.maxDiscountAmount());
        assertEquals(id, dto.id());
        assertEquals(typeName, dto.typeName());
    }
}