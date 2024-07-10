package com.nhnacademy.codequestweb.client.search;

import com.nhnacademy.codequestweb.response.search.SearchResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "search", url = "http://localhost:8001")
public interface SearchClient {
    @GetMapping("/api/search")
    ResponseEntity<Page<SearchResponseDto>> findAllBookProductDocuments(
            @RequestParam String keywords,
            @RequestParam int page,
            @RequestParam int size
    );
}
