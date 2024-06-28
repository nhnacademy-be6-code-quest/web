package com.nhnacademy.codequestweb.controller.product.adminOnly;

import com.nhnacademy.codequestweb.request.product.tag.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.tag.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagRegisterResponseDto;
import com.nhnacademy.codequestweb.service.product.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/admin/tags/registerForm")
    public String getRegisterForm(Model model) {
        model.addAttribute("view", "tagRegisterForm");
        return "index";
    }

    @PostMapping("/admin/tags/register")
    public String saveTag(@ModelAttribute TagRegisterRequestDto dto) {
        ResponseEntity<TagRegisterResponseDto> response = tagService.saveTag(dto);
        log.warn(response.toString());
        return "redirect:/";
    }

    @GetMapping("/tags/all")
    public String getAllTagPage(@RequestParam(name = "page", required = false)Integer page, @RequestParam(name = "desc", required = false)Boolean desc, Model model) {
        ResponseEntity<Page<TagGetResponseDto>> response = tagService.getAllTags(page, desc);
        model.addAttribute("tagPage", response.getBody());
//        model.addAttribute("view", "admin");
        return "/view/admin/tagPage";
    }

    @GetMapping("/tags/containing")
    public String getNameContainingTagPage(@RequestParam(name = "page", required = false)Integer page, @RequestParam(name = "desc", required = false)Boolean desc, @RequestParam("tagName") String tagName, Model model) {
        ResponseEntity<Page<TagGetResponseDto>> response = tagService.getNameContainingTagPage(page, desc, tagName);
        model.addAttribute("tagPage", response.getBody());
        return "/view/admin/tagPage";
    }
}
