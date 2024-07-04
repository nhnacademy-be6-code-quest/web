package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.tag.TagClient;
import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.request.product.tag.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.tag.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagRegisterResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagClient tagClient;

    public ResponseEntity<TagRegisterResponseDto> saveTag(HttpHeaders headers, TagRegisterRequestDto dto) {
        return tagClient.saveTag(headers, dto);
    }

    public ResponseEntity<Page<TagGetResponseDto>> getAllTags(Integer page, Boolean desc){
        return tagClient.getAllTags(page, desc);
    }

    public ResponseEntity<Page<TagGetResponseDto>> getNameContainingTagPage(Integer page, Boolean desc, String tagName){
        return tagClient.getNameContainingTagPage(page, desc, tagName);
    }
}
