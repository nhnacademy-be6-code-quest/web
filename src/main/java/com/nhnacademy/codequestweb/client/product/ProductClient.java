package com.nhnacademy.codequestweb.client.product;

import com.nhnacademy.codequestweb.response.product.AladinBookListResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ADMIN-SERVICE", url = "http://localhost:8001")
public interface ProductClient {

        @GetMapping("/api/admin/book")
        public ResponseEntity<AladinBookListResponseDto> getBookList(@RequestParam("title") String title);
}
