package com.nhnacademy.codequestweb.controller.search;

import com.nhnacademy.codequestweb.response.search.SearchResponseDto;
import com.nhnacademy.codequestweb.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/search/title")
    public ResponseEntity<List<String>> search(@RequestParam String keyword) {
        log.info("search keyword: " + keyword);
        List<String> titles = searchService.search(keyword, 0, 10).getContent().stream()
                .map(SearchResponseDto::getTitle)
                .toList();
        return ResponseEntity.ok(titles);
    }
}
