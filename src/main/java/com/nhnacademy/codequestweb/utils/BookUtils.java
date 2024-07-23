package com.nhnacademy.codequestweb.utils;

import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

@Slf4j
public class BookUtils {

    private BookUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void setBookPage (ResponseEntity<Page<BookProductGetResponseDto>> response, Integer page, String sort, Boolean desc, Model model) {
        if (response != null) {
            Page<BookProductGetResponseDto> responseDtoPage = response.getBody();
            if (responseDtoPage != null) {
                model.addAttribute("sort", sort);
                model.addAttribute("desc", desc);
                model.addAttribute("view", "productList");
                model.addAttribute("productList", responseDtoPage.getContent());
                page = page == null ? 1 : page;
                model.addAttribute("page", page);
                model.addAttribute("totalPage", responseDtoPage.getTotalPages());
            }else {
                log.warn("response body is null");
            }
        }else {
            log.warn("response is null");
        }
    }

    public static void setSingleBookInfo(ResponseEntity<BookProductGetResponseDto> response, Model model) {
        BookProductGetResponseDto bookProductGetResponseDto = response.getBody();
        if (bookProductGetResponseDto != null) {
            Set<ProductCategory> categorySet = bookProductGetResponseDto.categorySet();
            List<List<ProductCategory>> allCategoryList = new ArrayList<>();
            for (ProductCategory category : categorySet) {
                List<ProductCategory> parentCategoryList = new ArrayList<>();
                parentCategoryList.add(category);
                ProductCategory parent = category.parentProductCategory();
                while(parent != null) {
                    parentCategoryList.add(parent);
                    parent = parent.parentProductCategory();
                }
                parentCategoryList.sort(Comparator.comparing(ProductCategory::productCategoryId));
                allCategoryList.add(parentCategoryList);
            }

            model.addAttribute("book", bookProductGetResponseDto);
            model.addAttribute("listOfCategoryList", allCategoryList);
        }else {
            log.warn("single book response is null");
        }
    }
}

