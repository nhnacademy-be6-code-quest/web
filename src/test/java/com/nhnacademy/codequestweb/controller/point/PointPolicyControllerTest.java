package com.nhnacademy.codequestweb.controller.point;

import com.nhnacademy.codequestweb.request.point.PointPolicyActiveRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyModifyRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.service.point.PointPolicyService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PointPolicyControllerTest {

    @Mock
    private PointPolicyService pointPolicyService;

    @InjectMocks
    private PointPolicyController pointPolicyController;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access", "access")});
    }

    @Test
    void testPointPolicyRegister() {
        PointPolicyRegisterRequestDto dto = new PointPolicyRegisterRequestDto();

        String result = pointPolicyController.pointPolicyRegister(request, dto);

        assertEquals("redirect:/admin/point/policy", result);
        verify(pointPolicyService).savePointPolicy(any(HttpHeaders.class), eq(dto));
    }

    @Test
    void testPointPolicyModify() {
        PointPolicyModifyRequestDto dto = new PointPolicyModifyRequestDto(1L, 1L);

        String result = pointPolicyController.pointPolicyRegister(request, dto);

        assertEquals("redirect:/admin/point/policy", result);
        verify(pointPolicyService).modifyPolicy(any(HttpHeaders.class), eq(dto));
    }

    @Test
    void testPointPolicyActive() {
        PointPolicyActiveRequestDto dto = new PointPolicyActiveRequestDto();

        String result = pointPolicyController.pointPolicyActive(request, dto);

        assertEquals("redirect:/admin/point/policy", result);
        verify(pointPolicyService).pointActive(any(HttpHeaders.class), eq(dto));
    }
}