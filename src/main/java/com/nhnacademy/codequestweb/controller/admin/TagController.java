package com.nhnacademy.codequestweb.controller.admin;

import com.nhnacademy.codequestweb.request.bookProduct.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.TagRegisterResponseDto;
import com.nhnacademy.codequestweb.service.admin.TagRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagRegisterService tagRegisterService;

    @GetMapping
    public String getRegisterForm(Model model) {
        model.addAttribute("view", "tagRegisterForm");
        return "index";
    }

    @PostMapping
    public String saveTag(@ModelAttribute TagRegisterRequestDto dto) {
        ResponseEntity<TagRegisterResponseDto> response = tagRegisterService.saveTag(dto);
        log.warn(response.toString());
        return "redirect:/";
    }
}
