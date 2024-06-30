package com.nhnacademy.codequestweb.controller.product.adminOnly;

import com.nhnacademy.codequestweb.request.product.productCategory.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.service.product.CategoryService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/admin/categories/registerForm")
    public String getCategoryRegisterForm(Model model) {
        model.addAttribute("view", "categoryRegisterForm");
        return "index";
    }

    @PostMapping("/admin/categories/register")
    public String saveCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "parentCategoryName", required = false) String parentCategoryName,
            RedirectAttributes redirectAttributes,
            HttpServletRequest req
            ) {
        log.warn("category Name : {}, parent Category : {}", categoryName, parentCategoryName);
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req,"access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        CategoryRegisterRequestDto dto = new CategoryRegisterRequestDto(categoryName, parentCategoryName);
//        if (parentCategoryName.isBlank()){
//            dto = new CategoryRegisterRequestDto(categoryName, null);
//        }else{
//            dto = new CategoryRegisterRequestDto(categoryName, parentCategoryName);
//        }
        ResponseEntity<CategoryRegisterResponseDto> response = categoryService.saveCategory(headers, dto);
        log.info("status code : {} body : {}",response.getStatusCode().value(), response.getBody());
        redirectAttributes.addFlashAttribute("message", "category saved successfully");
        return "redirect:/";
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

    private List<String> getCategoryPathNameList(ResponseEntity<Page<CategoryGetResponseDto>> response) {
        List<String> categoryNamePage = new ArrayList<>();
        if (response.getBody() != null) {
            List<CategoryGetResponseDto> categoryList = response.getBody().getContent();
            for (CategoryGetResponseDto category : categoryList) {
                log.error("category id : {}",category.productCategoryId());
                categoryNamePage.add(getAllCategoryPathName(category));
            }
        }
        return categoryNamePage;
    }

    @GetMapping("/categories/containing")
    public String getCategoryContainingPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getNameContainingCategories(page, desc, sort, categoryName);
        List<String> categoryNamePage = getCategoryPathNameList(response);

        model.addAttribute("categoryNamePage", categoryNamePage);
        model.addAttribute("view", "categories");
        return "/view/product/categoryPage";

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


    private String getAllCategoryPathName1(CategoryGetResponseDto category) {
        StringBuilder stringBuilder = new StringBuilder();
        ProductCategory parentCategory = category.parentProductCategory();
        Stack<String> categoryStack = new Stack<>();
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

    @GetMapping("/categories/sub")
    public String getCategorySubPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getSubCategories(page, desc, sort, categoryName);
        List<CategoryGetResponseDto> categories = getCategoryPathList1(response);
        log.error("{}",response);
        log.error("{}",categories);

        //List<String> categoryNamePage = getCategoryPathNameList(response);
        model.addAttribute("categoryDetails", categories);
        //model.addAttribute("categorys",categoryNamePage);
       // return "/view/product/categoryPage";
        return "/test/second_popup";
    }

    @GetMapping("/categories/search")
    public String test() {
        return "/view/product/categorySearch";
    }
}
