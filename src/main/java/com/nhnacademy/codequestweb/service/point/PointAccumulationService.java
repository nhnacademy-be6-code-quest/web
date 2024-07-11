package com.nhnacademy.codequestweb.service.point;

import com.nhnacademy.codequestweb.client.point.PointAccumulationClient;
import com.nhnacademy.codequestweb.response.point.PointAccumulationAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationMyPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointAccumulationService {

    private final PointAccumulationClient pointAccumulationClient;

    public Page<PointAccumulationMyPageResponseDto> clientPoint(HttpHeaders headers, int page, int size){
        return pointAccumulationClient.findClientPoint(headers, page, size).getBody();
    }

    public Page<PointAccumulationAdminPageResponseDto> userPoint(HttpHeaders headers, int page, int size){
        return pointAccumulationClient.findUserPoint(headers, page, size).getBody();
    }
    public void deleteUserPoint(HttpHeaders headers, long pointAccumulationHistoryId){
        pointAccumulationClient.deleteuserPoint(headers,pointAccumulationHistoryId);
    }
}
