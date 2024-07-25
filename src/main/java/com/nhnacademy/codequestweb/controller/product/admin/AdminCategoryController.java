package com.nhnacademy.codequestweb.controller.product.admin;

import com.nhnacademy.codequestweb.config.CategoryConfig;
import com.nhnacademy.codequestweb.request.product.product_category.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.product_category.CategoryUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryNodeResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.ProductCategory;
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

    private static final String INDEX = "index";

    private static final String ADMIN_PAGE = "adminPage";

    private static final String CATEGORIES = "categories";

    private static final String REDIRECT_ADMIN_MAIN = "redirect:/admin/client/0";

    private static final String WINDOW_CLOSE = "view/product/window.close";

    private static final String ALTER_MESSAGE = "alterMessage";

    private static final String ATTRIBUTE_VALUE = "failed to get categories.";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
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

    private String makeView(Page<CategoryGetResponseDto> responsePage,
                            RedirectAttributes redirectAttributes,
                            Model model, Integer page, String sort, Boolean desc, String url) {
        if (responsePage == null){
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return "redirect:/";
        }else {
            List<CategoryGetResponseDto> categoryNameList = responsePage.getContent();
            Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();
            for (CategoryGetResponseDto category : categoryNameList) {
                categoryNameMap.put(category, getAllCategoryPathName(category));
            }
            model.addAttribute("categoryNamePage", categoryNameMap);
            long totalElements = responsePage.getTotalElements();
            model.addAttribute("totalCount", totalElements);
            if (totalElements == 0) {
                model.addAttribute("empty", true);
            }
            model.addAttribute("totalPage", responsePage.getTotalPages());
            model.addAttribute("page", page == null ? 1 : page);
            model.addAttribute("sort", sort);
            model.addAttribute("desc", desc);
            model.addAttribute("view", ADMIN_PAGE);
            model.addAttribute(ADMIN_PAGE, CATEGORIES);
            model.addAttribute("url", url);
            model.addAttribute("activeSection","book");
            model.addAttribute("categoryPage", responsePage);
            return INDEX;
        }
    }

    @GetMapping("/admin/categories")
    public String getCategories(
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getCategories(page, desc, sort);

            Page<CategoryGetResponseDto> responsePage = response.getBody();

            return makeView(responsePage, redirectAttributes, model, page, sort, desc, "/admin/categories?");
        }catch (FeignException e) {
            log.warn("error occurred while get all category, message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
    }

    @GetMapping("/admin/categories/containing")
    public String getCategoryContainingPage(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "desc", required = false) Boolean desc,
            RedirectAttributes redirectAttributes,
            @RequestParam("categoryName") String categoryName,
            Model model) {
        try {
            ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getNameContainingCategories(page, desc, sort, categoryName);

            Page<CategoryGetResponseDto> responsePage = response.getBody();

            return makeView(responsePage, redirectAttributes ,model, page, sort, desc, "/admin/categories/containing?title=" + categoryName + "&");
        }catch (FeignException e) {
            log.warn("error occurred while get name containing category with name {}, message : {}", categoryName, e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
    }

    @GetMapping("/admin/categories/{categoryId}/sub")
    public String getCategorySubPage(@RequestParam(name = "page", required = false) Integer page,
                                     @RequestParam(name = "sort", required = false) String sort,
                                     @RequestParam(name = "desc", required = false) Boolean desc,
                                     RedirectAttributes redirectAttributes,
                                     @PathVariable("categoryId") Long categoryId,
                                     Model model) {

        try {
            ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getSubCategories(page, desc, sort, categoryId);
            Page<CategoryGetResponseDto> responsePage = response.getBody();
            return makeView(responsePage, redirectAttributes, model, page, sort, desc, "/admin/categories/" + categoryId + "/sub?");
        }catch (FeignException e) {
            log.warn("error occurred while get sub category with id {}, message : {}", categoryId, e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
    }


    @PostMapping("/admin/categories/register")
    public ResponseEntity<Void> saveCategory(
            @ModelAttribute CategoryRegisterRequestDto dto,
            HttpServletRequest req
            ) {
        try {
            ResponseEntity<CategoryRegisterResponseDto> response = categoryService.saveCategory(CookieUtils.setHeader(req), dto);
            return ResponseEntity.status(response.getStatusCode().value()).body(null);
        }catch (FeignException e){
            log.warn("error occurred while save new category with request {}, message : {}", dto, e.getMessage());
            return ResponseEntity.status(e.status() != 0 ? e.status() : 500).build();
        }
    }

    @PutMapping("/admin/categories/update")
    public ResponseEntity<Void> updateCategory(
            @ModelAttribute CategoryUpdateRequestDto dto,
            HttpServletRequest req){
        try {
            ResponseEntity<CategoryUpdateResponseDto> response = categoryService.updateCategory(CookieUtils.setHeader(req), dto);
            return ResponseEntity.status(response.getStatusCode().value()).body(null);
        }catch (FeignException e){
            log.warn("error occurred while update category with request {}, message : {}", dto, e.getMessage());
            return ResponseEntity.status(e.status() != 0 ? e.status() : 500).build();
        }
    }

    @DeleteMapping("/admin/categories/delete/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId, HttpServletRequest req) {
        try {
            return categoryService.deleteCategory(CookieUtils.setHeader(req), categoryId);
        }catch (FeignException e){
            log.warn("error occurred while delete category with id {} (but it may be just it has relation with product), message : {}", categoryId, e.getMessage());
            return ResponseEntity.status(e.status() != 0 ? e.status() : 500).build();
        }
    }

    @PostMapping("/category/update")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryNodeResponseDto categoryNodeResponseDto) {
        categoryConfig.update(categoryNodeResponseDto);
        log.info("Category updated");
        return ResponseEntity.ok("Category updated");
    }


    private String makeModalView(Page<CategoryGetResponseDto> responsePage, Model model, boolean register, String url) {
        if (responsePage == null) {
            log.warn("error occurred while get categories. the response itself is null");
            return WINDOW_CLOSE;
        }else {
            List<CategoryGetResponseDto> categoryNamePage = responsePage.getContent();
            Map<CategoryGetResponseDto, String> categoryNameMap = new LinkedHashMap<>();
            Set<Integer> pageNumbers = new LinkedHashSet<>();
            pageNumbers.add(1);
            for (int i = 1; i <= responsePage.getTotalPages(); i++) {
                pageNumbers.add(i);
            }
            for (CategoryGetResponseDto category : categoryNamePage) {
                categoryNameMap.put(category, getAllCategoryPathName(category));
            }
            model.addAttribute("categoryNamePage", categoryNameMap);
            model.addAttribute("register", register);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("url", url);
            return "view/product/categoryPage";
        }
    }

    @GetMapping("/categories/all")
    public String getAllCategoriesPage(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "desc", required = false) Boolean desc,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {
        try {
            ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getCategories(page, desc, sort);
            Page<CategoryGetResponseDto> responsePage = response.getBody();
            return makeModalView(responsePage, model,  false, "/categories/all?page=");
        }catch (FeignException e){
            log.warn("error occurred while get all categories. message : {}", e.getMessage());
            return WINDOW_CLOSE;
        }
    }

    @GetMapping("/categories/containing")
    public String getContainingCategoriesPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName,  Model model) {
        try {
            ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getNameContainingCategories(page, desc, sort, categoryName);
            Page<CategoryGetResponseDto> responsePage = response.getBody();
            return makeModalView(responsePage, model,  false, "/categories/containing?categoryName="+categoryName +"&page=");
        }catch (FeignException e){
            log.warn("error occurred while get name containing (name : {}) categories. message : {}", categoryName, e.getMessage());
            return WINDOW_CLOSE;
        }
    }


    @GetMapping("/categories/{categoryId}/sub")
    public String getCategorySubAllPage(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "desc", required = false) Boolean desc,
            @RequestParam(name = "sort", required = false) String sort,
            @PathVariable("categoryId") Long categoryId, Model model){
        try {
            ResponseEntity<Page<CategoryGetResponseDto>> response = categoryService.getSubCategories(page, desc, sort, categoryId);

            Page<CategoryGetResponseDto> responsePage = response.getBody();

            return makeModalView(responsePage, model,true, "/categories/"+ categoryId + "/sub?page=");
        }catch (FeignException e){
            log.warn("error occurred while get sub (categoryId : {}) categories. message : {}", categoryId, e.getMessage());
            return WINDOW_CLOSE;
        }
    }
}
