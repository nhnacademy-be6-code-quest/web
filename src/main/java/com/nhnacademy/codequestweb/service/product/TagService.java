package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.tag.TagClient;
import com.nhnacademy.codequestweb.request.product.tag.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.tag.TagUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.tag.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagClient tagClient;

    public ResponseEntity<TagRegisterResponseDto> saveTag(HttpHeaders headers, TagRegisterRequestDto dto) {
        return tagClient.saveTag(headers, dto);
    }

    public ResponseEntity<TagUpdateResponseDto> updateTag(HttpHeaders headers, TagUpdateRequestDto dto) {
        return tagClient.updateTag(headers, dto);
    }

    public ResponseEntity<Void> deleteTag(HttpHeaders headers, Long tagId) {
        return tagClient.deleteTag(headers, tagId);
    }

    public ResponseEntity<Page<TagGetResponseDto>> getAllTags(HttpHeaders headers, Integer page, String sort, Boolean desc){
        return tagClient.getAllTags(headers, page, sort, desc);
    }

    public ResponseEntity<Page<TagGetResponseDto>> getNameContainingTagPage(HttpHeaders headers, Integer page, String sort, Boolean desc, String tagName){
        return tagClient.getNameContainingTagPage(headers, page, sort, desc, tagName);
    }
}
