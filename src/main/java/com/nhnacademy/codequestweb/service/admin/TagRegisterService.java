package com.nhnacademy.codequestweb.service.admin;

import com.nhnacademy.codequestweb.client.product.tag.TagRegisterClient;
import com.nhnacademy.codequestweb.request.bookProduct.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.TagRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagRegisterService {
    private final TagRegisterClient tagRegisterClient;

    public ResponseEntity<TagRegisterResponseDto> saveTag(TagRegisterRequestDto dto) {
        return tagRegisterClient.saveTag(dto);
    }
}
