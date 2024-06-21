package com.nhnacademy.codequestweb.controller.admin;

import com.nhnacademy.codequestweb.request.bookProduct.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.CategoryRegisterResponseDto;
import com.nhnacademy.codequestweb.service.admin.CategoryRegisterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryController {
    private final CategoryRegisterService categoryRegisterService;

    @GetMapping
    public String getCategoryRegisterForm(Model model) {
        model.addAttribute("view", "categoryRegisterForm");
        return "index";
    }

    @PostMapping
    public String saveCategory(@RequestParam("categoryName") String categoryName, @RequestParam("parentCategoryName") String parentCategoryName, RedirectAttributes redirectAttributes) {
        CategoryRegisterRequestDto dto;
        if (parentCategoryName.isBlank()){
            dto = new CategoryRegisterRequestDto(categoryName, null);
        }else{
            dto = new CategoryRegisterRequestDto(categoryName, parentCategoryName);
        }
        ResponseEntity<CategoryRegisterResponseDto> response = categoryRegisterService.saveCategory(dto);
        log.info("status code : {} body : {}",response.getStatusCode().value(), response.getBody());
        redirectAttributes.addFlashAttribute("message", "category saved successfully");
        return "redirect:/";
    }
}
