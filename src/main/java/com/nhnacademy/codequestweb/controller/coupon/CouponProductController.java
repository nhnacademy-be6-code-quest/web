package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.service.product.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Controller
@RequiredArgsConstructor
public class CouponProductController {

    private final CategoryService categoryService;
    private List<String> getCategoryPathNameList(ResponseEntity<Page<CategoryGetResponseDto>> response) {
        List<String> categoryNamePage = new ArrayList<>();
        if (response.getBody() != null) {
            List<CategoryGetResponseDto> categoryList = response.getBody().getContent();
            for (CategoryGetResponseDto category : categoryList) {

                categoryNamePage.add(getAllCategoryPathName(category));
            }
        }
        return categoryNamePage;
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
    private List<CategoryGetResponseDto> getCategoryPathList1(ResponseEntity<Page<CategoryGetResponseDto>> response) {
        List<CategoryGetResponseDto> categoryPathList = new ArrayList<>();
        if (response.getBody() != null) {
            List<CategoryGetResponseDto> categoryList = response.getBody().getContent();
            for (CategoryGetResponseDto category : categoryList) {
                CategoryGetResponseDto categoryPath = new CategoryGetResponseDto(
                        category.productCategoryId(),
                        getAllCategoryPathName(category),
                        category.parentProductCategory()
                );
                categoryPathList.add(categoryPath);
            }
        }
        return categoryPathList;
    }
    @GetMapping("/categories/sub")
    public String getCategorySubPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getSubCategories(page, desc, sort, categoryName);
        List<CategoryGetResponseDto> categories = getCategoryPathList1(response);

        //List<String> categoryNamePage = getCategoryPathNameList(response);
        model.addAttribute("categoryDetails", categories);
        //model.addAttribute("categorys",categoryNamePage);
        // return "/view/product/categoryPage";
        return "/test/second_popup";
    }
    @GetMapping("/all")
    public String getView() {
        return "/test/main";
    }
    @GetMapping("/categories/all")
    public String getAllCategoriesPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getCategories(page, desc, sort);
        List<String> categoryNamePage = getCategoryPathNameList(response);

        model.addAttribute("categoryNamePage", categoryNamePage);
        //return "/view/product/categoryPage";
        return "/test/first_popup";

    }
}
