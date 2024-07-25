package com.nhnacademy.codequestweb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.exception.product.NullPageException;
import com.nhnacademy.codequestweb.exception.product.ProductCategoryParsingException;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.ProductCategory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public static void setBookPage (Page<BookProductGetResponseDto> responseBody, Integer page, String sort, Boolean desc, Model model) {
        if (responseBody == null){
            throw new NullPageException();
        }else {
            model.addAttribute("sort", sort);
            model.addAttribute("desc", desc);
            model.addAttribute("view", "productList");
            model.addAttribute("productList", responseBody.getContent());
            page = page == null ? 1 : page;
            model.addAttribute("page", page);
            model.addAttribute("totalPage", responseBody.getTotalPages());
        }
    }

    public static void setSingleBookInfo(ResponseEntity<BookProductGetResponseDto> response, Model model) {
        BookProductGetResponseDto bookProductGetResponseDto = response.getBody();
        if (bookProductGetResponseDto != null) {
            Set<ProductCategory> categorySet = bookProductGetResponseDto.categorySet();
            List<List<ProductCategory>> allCategoryList = new ArrayList<>();
            for (ProductCategory category : categorySet) {
                List<ProductCategory> parentCategoryList = makeCategoryList(category);
                allCategoryList.add(parentCategoryList);
            }

            model.addAttribute("book", bookProductGetResponseDto);
            model.addAttribute("listOfCategoryList", allCategoryList);
        }else {
            log.warn("single book response is null");
        }
    }

    public static List<ProductCategory> makeCategoryList(ProductCategory category) {
        List<ProductCategory> categoryList = new ArrayList<>();
        categoryList.add(category);
        ProductCategory parent = category.parentProductCategory();
        while(parent != null) {
            categoryList.add(parent);
            parent = parent.parentProductCategory();
        }
        categoryList.sort(Comparator.comparing(ProductCategory::productCategoryId));
        return categoryList;
    }

    public static Page<BookProductGetResponseDto> getBookPageFromMap(ObjectMapper mapper, Map<String, Page<BookProductGetResponseDto>> responseBodyMap, Model model){
        if (responseBodyMap == null) {
            throw new NullPageException();
        }else {
            try {
                String categoryJson = responseBodyMap.keySet().stream().findFirst().orElseThrow(
                        NullPageException::new);
                ProductCategory category = mapper.readValue(categoryJson, ProductCategory.class);
                List<ProductCategory> parentCategoryList = BookUtils.makeCategoryList(category);
                model.addAttribute("categoryList", parentCategoryList);

                return responseBodyMap.values().stream().findFirst().orElseThrow(
                        NullPageException::new);
            }catch (JsonProcessingException e) {
                throw new ProductCategoryParsingException(e.getMessage());
            }
        }
    }
}

