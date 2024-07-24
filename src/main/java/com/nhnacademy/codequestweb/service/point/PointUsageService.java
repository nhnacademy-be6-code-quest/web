package com.nhnacademy.codequestweb.service.point;

import com.nhnacademy.codequestweb.client.point.PointUsageClient;
import com.nhnacademy.codequestweb.response.point.PointUsageAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageMyPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointUsageService {
    private final PointUsageClient pointUsageClient;

    public Page<PointUsageMyPageResponseDto> clientUsePoint(HttpHeaders headers, int page, int size){
        return pointUsageClient.usedClientPoint(headers, page, size).getBody();
    }

    public Page<PointUsageAdminPageResponseDto> userUsePoint(HttpHeaders headers, int page, int size){
        return pointUsageClient.usedUserPoint(headers, page, size).getBody();
    }
}
