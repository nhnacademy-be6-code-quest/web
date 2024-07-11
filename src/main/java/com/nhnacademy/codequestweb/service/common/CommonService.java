package com.nhnacademy.codequestweb.service.common;

import com.nhnacademy.codequestweb.client.common.CommonClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final CommonClient commonClient;

    public boolean isAdmin(String access) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("access", access);
        List<String> role = commonClient.getClientRole(headers).getBody().getRoles();
        return role.contains("ROLE_ADMIN");
    }
}
