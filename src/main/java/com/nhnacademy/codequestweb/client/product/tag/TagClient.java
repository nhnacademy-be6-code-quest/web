package com.nhnacademy.codequestweb.client.product.tag;

import com.nhnacademy.codequestweb.request.product.tag.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.tag.TagUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.tag.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagUpdateResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tag", url = "http://localhost:8001/api")
public interface TagClient {
    @PostMapping("/product/admin/tag/register")
    ResponseEntity<TagRegisterResponseDto> saveTag(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody TagRegisterRequestDto dto);

    @PutMapping("/product/admin/tag/update")
    ResponseEntity<TagUpdateResponseDto> updateTag(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody TagUpdateRequestDto dto);

    @DeleteMapping("/product/admin/tag/delete/{tagId}")
    ResponseEntity<Void> deleteTag(
            @RequestHeader HttpHeaders headers,
            @PathVariable("tagId") Long tagId);

    @GetMapping("/product/tags/all")
    ResponseEntity<Page<TagGetResponseDto>> getAllTags(
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "desc", required = false)Boolean desc);

    @GetMapping("/product/tags/containing")
    ResponseEntity<Page<TagGetResponseDto>> getNameContainingTagPage(
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam("tagName") String tagName);
}
