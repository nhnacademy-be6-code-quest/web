package com.nhnacademy.codequestweb.controller.admin;


import com.nhnacademy.codequestweb.client.product.ProductClient;
import com.nhnacademy.codequestweb.response.product.AladinBookListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {
    private final ProductClient productClient;

    @GetMapping("/book")
    public String addBook(@RequestParam("title") String title) {
        log.info("add book called");
        ResponseEntity<AladinBookListResponseDto> aladinBookListResponseDtoResponseEntity = productClient.getBookList(title);
        log.info(aladinBookListResponseDtoResponseEntity.getBody().toString());
        return "index";
    }
}
