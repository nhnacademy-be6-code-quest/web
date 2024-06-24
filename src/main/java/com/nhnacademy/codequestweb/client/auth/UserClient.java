package com.nhnacademy.codequestweb.client.auth;

import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterAddressRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "client", url = "http://localhost:8001")
public interface UserClient {
    @PostMapping("/api/client")
    ResponseEntity<ClientRegisterResponseDto> register(@Valid @RequestBody ClientRegisterRequestDto clientRegisterRequestDto);

    @GetMapping("/api/client")
    ResponseEntity<ClientPrivacyResponseDto> getPrivacy(@RequestHeader HttpHeaders headers);

    @GetMapping("/api/client/address")
    ResponseEntity<List<ClientDeliveryAddressResponseDto>> getDeliveryAddresses(@RequestHeader HttpHeaders headers);

    @PostMapping("/api/client/address")
    ResponseEntity<String> registerAddress(
            @RequestHeader HttpHeaders headers,
            @RequestBody ClientRegisterAddressRequestDto clientRegisterAddressRequestDto);

    @DeleteMapping("/api/client/address")
    ResponseEntity<String> deleteAddress(
            @RequestHeader HttpHeaders headers,
            @RequestParam Long addressId);
}
