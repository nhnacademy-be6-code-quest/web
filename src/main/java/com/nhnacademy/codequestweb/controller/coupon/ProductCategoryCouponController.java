package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.service.product.CategoryService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
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
    private final CategoryService categoryService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    @GetMapping("/admin/categories/all")
    public String getAllCategoriesPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getCategories(page, desc, sort);
        List<CategoryGetResponseDto> categoryNamePage = getCategoryPathNameList(response);
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();

        for (CategoryGetResponseDto category : categoryNamePage) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }
        model.addAttribute("categoryNamePage", categoryNameMap);
        model.addAttribute("register", false);
        return "view/coupon/categoryAdd";
    }

    private String getAllCategoryPathName(CategoryGetResponseDto category) {
        StringBuilder stringBuilder = new StringBuilder();
        ProductCategory parentCategory = category.parentProductCategory();
        Stack<String> categoryStack = new Stack<>();
        while (parentCategory != null){
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
        if (response.getBody() != null) {
            return response.getBody().getContent();
        }else{
            return new ArrayList<>();
        }
    }


    @GetMapping("/admin/categories/containing")
    public String getCategoryContainingPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getNameContainingCategories(page, desc, sort, categoryName);
        List<CategoryGetResponseDto> categoryNamePage = getCategoryPathNameList(response);
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();

        for (CategoryGetResponseDto category : categoryNamePage) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }

        model.addAttribute("register", false);
        model.addAttribute("categoryNamePage", categoryNameMap);
        return "view/coupon/categoryAdd";
    }

    @GetMapping("/admin/categories/{categoryId}/sub")
    public String getCategorySubPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @PathVariable("categoryId") Long categoryId, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getSubCategories(page, desc, sort, categoryId);
        List<CategoryGetResponseDto> categoryNamePage = getCategoryPathNameList(response);
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();

        for (CategoryGetResponseDto category : categoryNamePage) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }
        model.addAttribute("register", true);
        model.addAttribute("categoryNamePage", categoryNameMap);
        return "view/coupon/categoryAdd";
    }
}