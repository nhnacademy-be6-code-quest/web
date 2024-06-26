package com.nhnacademy.codequestweb.service.mypage;

import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterAddressRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterPhoneNumberRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserClient client;

    public ResponseEntity<ClientRegisterResponseDto> register(ClientRegisterRequestDto clientRegisterRequestDto) {
        return client.register(clientRegisterRequestDto);
    }

    public ResponseEntity<ClientPrivacyResponseDto> getPrivacy(HttpHeaders headers) {
        return client.getPrivacy(headers);
    }

    public ResponseEntity<List<ClientDeliveryAddressResponseDto>> getDeliveryAddresses(HttpHeaders headers) {
        return client.getDeliveryAddresses(headers);
    }

    public ResponseEntity<String> registerAddress(HttpHeaders headers, ClientRegisterAddressRequestDto clientRegisterAddressRequestDto) {
        return client.registerAddress(headers, clientRegisterAddressRequestDto);
    }

    public ResponseEntity<String> deleteDeliveryAddress(HttpHeaders headers, Long deliveryAddressId) {
        return client.deleteAddress(headers, deliveryAddressId);
    }

    public ResponseEntity<String> deleteClient(HttpHeaders headers) {
        return client.deleteClient(headers);
    }

    public ResponseEntity<List<ClientPhoneNumberResponseDto>> getPhoneNumbers(HttpHeaders headers) {
        return client.getPhoneNumber(headers);
    }

    public ResponseEntity<String> registerPhoneNumbers(HttpHeaders headers, @RequestBody ClientRegisterPhoneNumberRequestDto clientRegisterPhoneNumberDto) {
        return client.registerPhoneNumber(headers, clientRegisterPhoneNumberDto);
    }

    public ResponseEntity<String> deletePhoneNumber(HttpHeaders headers, Long phoneNumberId) {
        return client.deletePhoneNumber(headers, phoneNumberId);
    }
}
