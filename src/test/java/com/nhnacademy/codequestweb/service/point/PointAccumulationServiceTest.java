package com.nhnacademy.codequestweb.service.point;

import com.nhnacademy.codequestweb.client.point.PointAccumulationClient;
import com.nhnacademy.codequestweb.response.point.PointAccumulationAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationMyPageResponseDto;
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

class PointAccumulationServiceTest {

    @Mock
    private PointAccumulationClient pointAccumulationClient;

    @InjectMocks
    private PointAccumulationService pointAccumulationService;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        headers = new HttpHeaders();
    }

    @Test
    void testClientPoint() {
        int page = 0;
        int size = 10;

        List<PointAccumulationMyPageResponseDto> pointList = new ArrayList<>();
        pointList.add(new PointAccumulationMyPageResponseDto(/* 필요한 파라미터 */));
        Page<PointAccumulationMyPageResponseDto> mockPage = new PageImpl<>(pointList);

        when(pointAccumulationClient.findClientPoint(headers, page, size))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<PointAccumulationMyPageResponseDto> result = pointAccumulationService.clientPoint(headers, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(pointAccumulationClient).findClientPoint(headers, page, size);
    }

    @Test
    void testUserPoint() {
        int page = 0;
        int size = 10;

        List<PointAccumulationAdminPageResponseDto> pointList = new ArrayList<>();
        pointList.add(new PointAccumulationAdminPageResponseDto(/* 필요한 파라미터 */));
        Page<PointAccumulationAdminPageResponseDto> mockPage = new PageImpl<>(pointList);

        when(pointAccumulationClient.findUserPoint(headers, page, size))
                .thenReturn(ResponseEntity.ok(mockPage));

        Page<PointAccumulationAdminPageResponseDto> result = pointAccumulationService.userPoint(headers, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(pointAccumulationClient).findUserPoint(headers, page, size);
    }
}