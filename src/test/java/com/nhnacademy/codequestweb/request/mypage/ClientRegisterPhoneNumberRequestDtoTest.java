package com.nhnacademy.codequestweb.request.mypage;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClientRegisterPhoneNumberRequestDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgsConstructor() {
        ClientRegisterPhoneNumberRequestDto dto = new ClientRegisterPhoneNumberRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testGetPhoneNumber() {
        ClientRegisterPhoneNumberRequestDto dto = new ClientRegisterPhoneNumberRequestDto();
        dto.setPhoneNumber("01012345678");
        assertEquals("01012345678", dto.getPhoneNumber());
    }

    @Test
    void testSetPhoneNumber() {
        ClientRegisterPhoneNumberRequestDto dto = new ClientRegisterPhoneNumberRequestDto();
        dto.setPhoneNumber("01012345679");
        assertEquals("01012345679", dto.getPhoneNumber());
    }

    @Test
    void testValidPhoneNumber() {
        ClientRegisterPhoneNumberRequestDto dto = new ClientRegisterPhoneNumberRequestDto();
        dto.setPhoneNumber("01012345678");

        Set<ConstraintViolation<ClientRegisterPhoneNumberRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPhoneNumberNotNullAndNotBlank() {
        ClientRegisterPhoneNumberRequestDto dto = new ClientRegisterPhoneNumberRequestDto();
        dto.setPhoneNumber(null);

        Set<ConstraintViolation<ClientRegisterPhoneNumberRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        dto = new ClientRegisterPhoneNumberRequestDto();
        dto.setPhoneNumber("");

        violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPhoneNumberSize() {
        ClientRegisterPhoneNumberRequestDto dto = new ClientRegisterPhoneNumberRequestDto();
        dto.setPhoneNumber("010123456");

        Set<ConstraintViolation<ClientRegisterPhoneNumberRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<ClientRegisterPhoneNumberRequestDto> violation = violations.iterator().next();
        assertEquals("전화번호는 '-'없이 10 또는 11자리의 수 이어야합니다.", violation.getMessage());
    }

    @Test
    void testPhoneNumberDigits() {
        ClientRegisterPhoneNumberRequestDto dto = new ClientRegisterPhoneNumberRequestDto();
        dto.setPhoneNumber("0101234567a");

        Set<ConstraintViolation<ClientRegisterPhoneNumberRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
