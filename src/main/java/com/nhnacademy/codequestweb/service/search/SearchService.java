package com.nhnacademy.codequestweb.service.search;

import com.nhnacademy.codequestweb.client.search.SearchClient;
import com.nhnacademy.codequestweb.response.search.SearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchClient searchClient;

    public Page<SearchResponseDto> search(String keyword, int page, int size) {
        return searchClient.findAllBookProductDocuments(keyword, page, size).getBody();
    }
}
