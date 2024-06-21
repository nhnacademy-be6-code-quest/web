package com.nhnacademy.codequestweb.client.product.tag;

import com.nhnacademy.codequestweb.request.bookProduct.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.Tag;
import com.nhnacademy.codequestweb.response.product.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.TagRegisterResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "TAG-SERVICE", url = "http://localhost:8001/api/admin/tag")
public interface TagRegisterClient {
    @PostMapping
    ResponseEntity<TagRegisterResponseDto> saveTag(@Valid @RequestBody TagRegisterRequestDto dto);

    @GetMapping("/list")
    ResponseEntity<List<TagGetResponseDto>> getAllTags();
}
