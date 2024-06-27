package com.nhnacademy.codequestweb.client.product.tag;

import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.request.product.tag.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.tag.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagRegisterResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tag", url = "http://localhost:8001/api")
public interface TagClient {
    @PostMapping("/product/admin/tag/register")
    ResponseEntity<TagRegisterResponseDto> saveTag(@Valid @RequestBody TagRegisterRequestDto dto);

    @GetMapping("/product/tags/all")
    ResponseEntity<Page<TagGetResponseDto>> getAllTags(@RequestParam(name = "page", required = false)Integer page, @RequestParam(name = "desc", required = false)Boolean desc);


    @GetMapping("/product/tags/containing")
    ResponseEntity<Page<TagGetResponseDto>> getNameContainingTagPage(@RequestParam(name = "page", required = false)Integer page, @RequestParam(name = "desc", required = false)Boolean desc, @RequestParam("tagName") String tagName);
}
