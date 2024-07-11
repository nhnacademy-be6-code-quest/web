package com.nhnacademy.codequestweb.service.admin;

import com.nhnacademy.codequestweb.client.admin.AdminClient;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminClient adminClient;

    public Page<ClientPrivacyResponseDto> privacyList(int page, int size, String sort, boolean desc, String access) {
        return adminClient.privacyPage(page, size, sort, desc, access).getBody();
    }
}
