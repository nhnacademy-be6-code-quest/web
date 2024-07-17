package com.nhnacademy.codequestweb.controller.product.adminOnly;

import com.nhnacademy.codequestweb.config.CategoryConfig;
import com.nhnacademy.codequestweb.request.product.productCategory.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.productCategory.CategoryUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryNodeResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.service.product.CategoryService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    private final CategoryConfig categoryConfig;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/admin/categories")
    public String getCategories(@RequestParam(name = "page", required = false)Integer page, @RequestParam(name = "sort", required = false) String sort, @RequestParam(name = "desc", required = false)Boolean desc, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getCategories(page, desc, sort);
        List<CategoryGetResponseDto> categoryNameList = response.getBody().getContent();
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();
        for (CategoryGetResponseDto category : categoryNameList) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }
        model.addAttribute("categoryNamePage", categoryNameMap);
        long totalElements = response.getBody().getTotalElements();
        model.addAttribute("totalCount", totalElements);
        if (totalElements == 0) {
            model.addAttribute("empty", true);
        }
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("page", page == null ? 1 : page);
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("view", "adminPage");
        model.addAttribute("adminPage", "categories");
        model.addAttribute("url", "/admin/categories?");
        model.addAttribute("activeSection","book");
        return "index";
    }

    @GetMapping("/admin/categories/containing")
    public String getCategoryContainingPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getNameContainingCategories(page, desc, sort, categoryName);
        Page<CategoryGetResponseDto> categoryNamePage = response.getBody();

        List<CategoryGetResponseDto> categoryNameList = categoryNamePage.getContent();
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();
        Set<Integer> pageNumbers = new LinkedHashSet<>();
        pageNumbers.add(1);
        for (int i = 0; i < response.getBody().getTotalPages(); i++) {
            pageNumbers.add(i);
        }
        for (CategoryGetResponseDto category : categoryNameList) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }

        long totalElements = response.getBody().getTotalElements();
        model.addAttribute("totalCount", totalElements);
        if (totalElements == 0) {
            model.addAttribute("empty", true);
        }
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("page", page == null ? 1 : page);
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("categoryPage", response.getBody());
        model.addAttribute("view", "adminPage");
        model.addAttribute("adminPage", "categories");
        model.addAttribute("url", "/admin/categories/containing?title=" + categoryName + "&");
        return "index";
    }


    @GetMapping("/admin/categories/registerForm")
    public String getCategoryRegisterForm(Model model) {
        model.addAttribute("action", "register");
        return "view/product/categoryForm";
    }

    @GetMapping("/admin/categories/updateForm")
    public String getCategoryUpdateForm(Model model) {
        model.addAttribute("action", "update");
        return "view/product/categoryForm";
    }

    @PostMapping("/admin/categories/register")
    public ResponseEntity<Void> saveCategory(
            @ModelAttribute CategoryRegisterRequestDto dto,
            HttpServletRequest req
            ) {
        log.info("trying register new category - parent : {} , new : {}", dto.parentCategoryName(), dto.categoryName());
        try {
            ResponseEntity<CategoryRegisterResponseDto> response = categoryService.saveCategory(CookieUtils.setHeader(req), dto);
            return ResponseEntity.ok(null);
        }catch (FeignException e){
            log.warn(e.getMessage());
            return switch (e.status()) {
                case 303 -> ResponseEntity.status(303).body(null);
                case 401, 403 -> ResponseEntity.status(401).body(null);
                case 405 -> ResponseEntity.status(405).body(null);
                case 409 -> ResponseEntity.status(409).body(null);
                default -> ResponseEntity.status(500).body(null);
            };
        }
    }

    @PutMapping("/admin/categories/update")
    public ResponseEntity<Void> updateCategory(
            @ModelAttribute CategoryUpdateRequestDto dto,
            HttpServletRequest req){
        try {
            ResponseEntity<CategoryUpdateResponseDto> response = categoryService.updateCategory(CookieUtils.setHeader(req), dto);
            return ResponseEntity.ok(null);
        }catch (FeignException e){
            log.warn(e.getMessage());
            return switch (e.status()) {
                case 303 -> ResponseEntity.status(303).body(null);
                case 401, 403 -> ResponseEntity.status(401).body(null);
                case 405 -> ResponseEntity.status(405).body(null);
                case 409 -> ResponseEntity.status(409).body(null);
                default -> ResponseEntity.status(500).body(null);
            };
        }
    }

    @PostMapping("/category/update")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryNodeResponseDto categoryNodeResponseDto) {
        categoryConfig.update(categoryNodeResponseDto);
        log.info("Category updated");
        return ResponseEntity.ok("Category updated");
    }


    @DeleteMapping("/admin/categories/delete/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId, HttpServletRequest req) {
        try {
            return categoryService.deleteCategory(CookieUtils.setHeader(req), categoryId);
        }catch (FeignException e){
            log.warn(e.getMessage());
            return switch (e.status()) {
                case 409 -> ResponseEntity.status(409).body(null);
                default -> ResponseEntity.status(500).body(null);
            };
        }
    }


    @GetMapping("/categories/all")
    public String getAllCategoriesPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getCategories(page, desc, sort);
        List<CategoryGetResponseDto> categoryNamePage = response.getBody().getContent();
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();
        Set<Integer> pageNumbers = new LinkedHashSet<>();
        pageNumbers.add(1);
        for (int i = 0; i < response.getBody().getTotalPages(); i++) {
            pageNumbers.add(i);
        }
        for (CategoryGetResponseDto category : categoryNamePage) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }
        model.addAttribute("categoryNamePage", categoryNameMap);
        model.addAttribute("register", false);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("url", "/categories/all?page=");
        return "view/product/categoryPage";
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




    @GetMapping("/admin/categories/{categoryId}/sub")
    public String getCategorySubPage(@RequestParam(name = "page", required = false) Integer page,
                                     @RequestParam(name = "desc", required = false) Boolean desc,
                                     @RequestParam(name = "sort", required = false) String sort,
                                     @PathVariable("categoryId") Long categoryId,
                                     Model model) {
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getSubCategories(page, desc, sort, categoryId);
        List<CategoryGetResponseDto> categoryNameList = response.getBody().getContent();
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();
        for (CategoryGetResponseDto category : categoryNameList) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }
        model.addAttribute("categoryNamePage", categoryNameMap);
        long totalElements = response.getBody().getTotalElements();
        model.addAttribute("totalCount", totalElements);
        if (totalElements == 0) {
            model.addAttribute("empty", true);
        }
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("page", page == null ? 1 : page);
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("categoryPage", response.getBody());
        model.addAttribute("view", "adminPage");
        model.addAttribute("adminPage", "categories");
        model.addAttribute("url", "/admin/categories/" + categoryId + "/sub?");
        return "index";
    }

    @GetMapping("/categories/{categoryId}/sub")
    public String getCategorySubAllPage(@PathVariable("categoryId") Long categoryId, Model model){
        ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getSubCategories(null, null, null, categoryId);
        List<CategoryGetResponseDto> categoryNameList = response.getBody().getContent();
        Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();
        for (CategoryGetResponseDto category : categoryNameList) {
            categoryNameMap.put(category, getAllCategoryPathName(category));
        }
        model.addAttribute("categoryNamePage", categoryNameMap);
        model.addAttribute("register", true);
        return "view/product/categoryPage";
    }

    @GetMapping("/categories/search")
    public String test() {
        return "view/product/categorySearch";
    }
}
