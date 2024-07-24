package com.nhnacademy.codequestweb.service.point;

import com.nhnacademy.codequestweb.client.point.PointPolicyClient;
import com.nhnacademy.codequestweb.request.point.PointPolicyActiveRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyModifyRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.point.PointPolicyAdminListResponseDto;
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

class PointPolicyServiceTest {

    @Mock
    private PointPolicyClient pointPolicyClient;

    @InjectMocks
    private PointPolicyService pointPolicyService;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        headers = new HttpHeaders();
    }

    @Test
    void testSavePointPolicy() {
        PointPolicyRegisterRequestDto requestDto = new PointPolicyRegisterRequestDto(/* 필요한 파라미터 */);

        pointPolicyService.savePointPolicy(headers, requestDto);

        verify(pointPolicyClient).savePointPolicy(headers, requestDto);
    }

    @Test
    void testFindPointPolicies() {
        int page = 0;
        int size = 10;

        List<PointPolicyAdminListResponseDto> policyList = new ArrayList<>();
        policyList.add(new PointPolicyAdminListResponseDto(/* 필요한 파라미터 */));
        Page<PointPolicyAdminListResponseDto> mockPage = new PageImpl<>(policyList);

        when(pointPolicyClient.findAllPointPolicy(headers, page, size))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<PointPolicyAdminListResponseDto> result = pointPolicyService.findPointPolicies(headers, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(pointPolicyClient).findAllPointPolicy(headers, page, size);
    }

    @Test
    void testModifyPolicy() {
        PointPolicyModifyRequestDto requestDto = new PointPolicyModifyRequestDto(1L, 1L);

        pointPolicyService.modifyPolicy(headers, requestDto);

        verify(pointPolicyClient).modifyPointPolicy(headers, requestDto);
    }

    @Test
    void testPointActive() {
        PointPolicyActiveRequestDto requestDto = new PointPolicyActiveRequestDto(/* 필요한 파라미터 */);

        pointPolicyService.pointActive(headers, requestDto);

        verify(pointPolicyClient).pointPolicyActive(headers, requestDto);
    }
}