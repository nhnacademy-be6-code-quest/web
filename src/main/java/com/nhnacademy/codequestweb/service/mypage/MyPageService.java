package com.nhnacademy.codequestweb.service.mypage;

import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserClient client;

    public ResponseEntity<ClientRegisterResponseDto> register(ClientRegisterRequestDto clientRegisterRequestDto) {
        return client.register(clientRegisterRequestDto);
    }

    public ResponseEntity<ClientPrivacyResponseDto> getPrivacy(@RequestHeader HttpHeaders headers) {
        return client.getPrivacy(headers);
    }

    public ResponseEntity<List<ClientDeliveryAddressResponseDto>> getDeliveryAddresses(@RequestHeader HttpHeaders headers) {
        return client.getDeliveryAddresses(headers);
    }
}
