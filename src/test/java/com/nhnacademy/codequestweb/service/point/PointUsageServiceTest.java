package com.nhnacademy.codequestweb.service.point;

import com.nhnacademy.codequestweb.client.point.PointUsageClient;
import com.nhnacademy.codequestweb.response.point.PointUsageAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageMyPageResponseDto;
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

class PointUsageServiceTest {

    @Mock
    private PointUsageClient pointUsageClient;

    @InjectMocks
    private PointUsageService pointUsageService;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        headers = new HttpHeaders();
    }

    @Test
    void testClientUsePoint() {
        int page = 0;
        int size = 10;

        List<PointUsageMyPageResponseDto> pointList = new ArrayList<>();
        pointList.add(new PointUsageMyPageResponseDto(/* 필요한 파라미터 */));
        Page<PointUsageMyPageResponseDto> mockPage = new PageImpl<>(pointList);

        when(pointUsageClient.usedClientPoint(headers, page, size))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<PointUsageMyPageResponseDto> result = pointUsageService.clientUsePoint(headers, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(pointUsageClient).usedClientPoint(headers, page, size);
    }

    @Test
    void testUserUsePoint() {
        int page = 0;
        int size = 10;

        List<PointUsageAdminPageResponseDto> pointList = new ArrayList<>();
        pointList.add(new PointUsageAdminPageResponseDto(/* 필요한 파라미터 */));
        Page<PointUsageAdminPageResponseDto> mockPage = new PageImpl<>(pointList);

        when(pointUsageClient.usedUserPoint(headers, page, size))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<PointUsageAdminPageResponseDto> result = pointUsageService.userUsePoint(headers, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(pointUsageClient).usedUserPoint(headers, page, size);
    }
}