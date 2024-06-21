package com.nhnacademy.codequestweb.service.admin;

import com.nhnacademy.codequestweb.client.product.bookProduct.BookProductRegisterClient;
import com.nhnacademy.codequestweb.client.product.category.CategoryRegisterClient;
import com.nhnacademy.codequestweb.client.product.tag.TagRegisterClient;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.AladinBookListResponseDto;
import com.nhnacademy.codequestweb.response.product.BookProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.Category;
import com.nhnacademy.codequestweb.response.product.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.Tag;
import com.nhnacademy.codequestweb.response.product.TagGetResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookProductRegisterService {
    private final BookProductRegisterClient bookProductRegisterClient;

    private final CategoryRegisterClient categoryRegisterClient;

    private final TagRegisterClient tagRegisterClient;

    public ResponseEntity<BookProductRegisterResponseDto> saveBook(BookProductRegisterRequestDto bookProductRegisterRequestDto) {
        return bookProductRegisterClient.saveBook(bookProductRegisterRequestDto);
    }

    public ResponseEntity<AladinBookListResponseDto> getBookList(String title) {
        return bookProductRegisterClient.getBookList(title);
    }

    public ResponseEntity<List<CategoryGetResponseDto>> getCategories() {
        return categoryRegisterClient.getAllCategories();
    }

    public ResponseEntity<List<TagGetResponseDto>> getTags(){
        return tagRegisterClient.getAllTags();
    }

}
