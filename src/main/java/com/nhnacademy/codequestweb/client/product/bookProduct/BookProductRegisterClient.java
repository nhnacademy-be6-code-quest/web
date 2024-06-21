package com.nhnacademy.codequestweb.client.product.bookProduct;

import com.nhnacademy.codequestweb.request.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.AladinBookListResponseDto;
import com.nhnacademy.codequestweb.response.product.BookProductRegisterResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ADMIN-SERVICE", url = "http://localhost:8001")
public interface BookProductRegisterClient {

        @GetMapping("/api/admin/book")
        ResponseEntity<AladinBookListResponseDto> getBookList(@RequestParam("title") String title);

        @PostMapping("/api/admin/book/register")
        ResponseEntity<BookProductRegisterResponseDto> saveBook(@RequestBody BookProductRegisterRequestDto bookProductRegisterRequestDto);
}
