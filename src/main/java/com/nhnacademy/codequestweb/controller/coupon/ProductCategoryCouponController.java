package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.response.product.product_category.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.ProductCategory;
import com.nhnacademy.codequestweb.service.product.CategoryService;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductCategoryCouponController {
    private static final String VIEW_COUPON_CATEGORY_ADD = "view/coupon/categoryAdd";
    private static final String CATEGORY_NAME_PAGE = "categoryNamePage";
    private static final String REGISTER = "register";

    private final CategoryService categoryService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    @GetMapping("/coupon/categories/all")
    public String getAllCategoriesPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getCategories(page, desc, sort);
        List<CategoryGetResponseDto> categoryNamePage = getCategoryPathNameList(response);
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();

        for (CategoryGetResponseDto category : categoryNamePage) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }
        model.addAttribute(CATEGORY_NAME_PAGE, categoryNameMap);
        model.addAttribute(REGISTER, false);
        return VIEW_COUPON_CATEGORY_ADD;
    }

    private String getAllCategoryPathName(CategoryGetResponseDto category) {
        StringBuilder stringBuilder = new StringBuilder();
        ProductCategory parentCategory = category.parentProductCategory();
        Deque<String> categoryStack = new ArrayDeque<>();
        while (parentCategory != null) {
            categoryStack.push(parentCategory.categoryName());
            parentCategory = parentCategory.parentProductCategory();
        }
        while (!categoryStack.isEmpty()) {
            stringBuilder.append(categoryStack.pop());
            stringBuilder.append("  >  ");
        }
        stringBuilder.append(category.categoryName());
        return stringBuilder.toString();
    }

    private List<CategoryGetResponseDto> getCategoryPathNameList(ResponseEntity<Page<CategoryGetResponseDto>> response) {
        Page<CategoryGetResponseDto> categoryPage = response.getBody();
        if (categoryPage != null) {
            return categoryPage.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/coupon/categories/containing")
    public String getCategoryContainingPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getNameContainingCategories(page, desc, sort, categoryName);
        List<CategoryGetResponseDto> categoryNamePage = getCategoryPathNameList(response);
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();

        for (CategoryGetResponseDto category : categoryNamePage) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }

        model.addAttribute(REGISTER, false);
        model.addAttribute(CATEGORY_NAME_PAGE, categoryNameMap);
        return VIEW_COUPON_CATEGORY_ADD;
    }

    @GetMapping("/coupon/categories/{categoryId}/sub")
    public String getCategorySubPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @PathVariable("categoryId") Long categoryId, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getSubCategories(page, desc, sort, categoryId);
        List<CategoryGetResponseDto> categoryNamePage = getCategoryPathNameList(response);
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();

        for (CategoryGetResponseDto category : categoryNamePage) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }
        model.addAttribute(REGISTER, true);
        model.addAttribute(CATEGORY_NAME_PAGE, categoryNameMap);
        return VIEW_COUPON_CATEGORY_ADD;
    }
}
