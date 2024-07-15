package com.nhnacademy.codequestweb.service.point;

import com.nhnacademy.codequestweb.client.point.PointPolicyClient;
import com.nhnacademy.codequestweb.request.point.PointPolicyActiveRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyModifyRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.point.PointPolicyAdminListResponseDto;
import com.nhnacademy.codequestweb.response.point.PointPolicyDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointPolicyService {

    private final PointPolicyClient pointPolicyClient;

    public void savePointPolicy(HttpHeaders headers, PointPolicyRegisterRequestDto pointPolicyRegisterRequestDto){
        pointPolicyClient.savePointPolicy(headers, pointPolicyRegisterRequestDto);
    }
    public Page<PointPolicyAdminListResponseDto> findPointPolicies(HttpHeaders headers, int page, int size){
        return pointPolicyClient.findAllPointPolicy(headers, page, size).getBody();
    }


    public PointPolicyDetailResponseDto findPolicy (long pointPolicyId){
        return pointPolicyClient.findPointPolicy(pointPolicyId).getBody();
    }
    public void modifyPolicy(PointPolicyModifyRequestDto pointPolicyModifyRequestDto){
        pointPolicyClient.modifyPointPolicy(pointPolicyModifyRequestDto);
    }
    public void pointActive(PointPolicyActiveRequestDto pointPolicyActiveRequestDto){
        pointPolicyClient.pointPolicyActive(pointPolicyActiveRequestDto);
    }


}
