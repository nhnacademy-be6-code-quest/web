package com.nhnacademy.codequestweb.controller.product.adminOnly;

import com.nhnacademy.codequestweb.request.product.tag.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.tag.TagUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.tag.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.TagService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;


    @GetMapping("/admin/tags")
    public String getTags(@RequestParam(name = "page", required = false)Integer page, @RequestParam(name = "sort", required = false) String sort, @RequestParam(name = "desc", required = false)Boolean desc, Model model) {
        ResponseEntity<Page<TagGetResponseDto>> response = tagService.getAllTags(page, sort, desc);
        long totalElements = response.getBody().getTotalElements();
        model.addAttribute("totalCount", totalElements);
        if (totalElements == 0) {
            model.addAttribute("empty", true);
        }
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("page", page == null ? 1 : page);
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("tagPage", response.getBody());
        model.addAttribute("view", "adminPage");
        model.addAttribute("adminPage", "tags");
        model.addAttribute("url", "/admin/tags?");
        model.addAttribute("activeSection","book");
        return "index";
    }

    @GetMapping("/admin/tags/containing")
    public String getNameContainingTags(@RequestParam(name = "page", required = false)Integer page, @RequestParam(name = "sort", required = false) String sort, @RequestParam(name = "desc", required = false)Boolean desc, @RequestParam("tagName") String tagName, Model model) {
        ResponseEntity<Page<TagGetResponseDto>> response = tagService.getNameContainingTagPage(page, sort, desc, tagName);
        long totalElements = response.getBody().getTotalElements();
        model.addAttribute("totalCount", totalElements);
        if (totalElements == 0) {
            model.addAttribute("empty", true);
        }
        model.addAttribute("tagPage", response.getBody());
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("page", page == null ? 1 : page);
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("view", "adminPage");
        model.addAttribute("adminPage", "tags");
        model.addAttribute("url", "/admin/tags/containing?tagName=" + tagName + "&");
        return "index";
    }

    //도서 등록시 불러야 함 일단 냅두기.
    @GetMapping("/tags/all")
    public String getRegisterForm(Model model) {
        ResponseEntity<Page<TagGetResponseDto>> response = tagService.getAllTags(null, null, null);
        model.addAttribute("tagPage", response.getBody());
        return "view/product/tagPage";
    }

    @PostMapping("/admin/tags/register")
    public ResponseEntity<Void> saveTag(HttpServletRequest req, @ModelAttribute TagRegisterRequestDto dto) {
        try {
            ResponseEntity<TagRegisterResponseDto> response = tagService.saveTag(CookieUtils.setHeader(req), dto);
            return ResponseEntity.ok(null);
        }catch (FeignException e){
            log.warn(e.getMessage());
            return switch (e.status()) {
                case 303 -> throw e;
                case 401, 403 -> ResponseEntity.status(401).body(null);
                case 409 -> ResponseEntity.status(409).body(null);
                default -> ResponseEntity.status(500).body(null);
            };
        }
    }

    @PutMapping("/admin/tags/update")
    public ResponseEntity<Void> updateTag(HttpServletRequest req, @ModelAttribute TagUpdateRequestDto dto) {
        try {
            ResponseEntity<TagUpdateResponseDto> response = tagService.updateTag(CookieUtils.setHeader(req), dto);
            return ResponseEntity.ok(null);
        }catch (FeignException e){
            log.warn(e.getMessage());
            return switch (e.status()) {
                case 303 -> throw e;
                case 401, 403 -> ResponseEntity.status(401).body(null);
                case 405 -> ResponseEntity.status(405).body(null);
                case 409 -> ResponseEntity.status(409).body(null);
                default -> ResponseEntity.status(500).body(null);
            };
        }
    }

    @DeleteMapping("/admin/tags/delete/{tagId}")
    public ResponseEntity<Void> deleteTag(HttpServletRequest req, @PathVariable Long tagId) {
        try {
            return tagService.deleteTag(CookieUtils.setHeader(req), tagId);
        }catch (FeignException e){
            log.warn(e.getMessage());
            return switch (e.status()) {
                case 303 -> throw e;
                case 401, 403 -> ResponseEntity.status(401).body(null);
                case 405 -> ResponseEntity.status(405).body(null);
                case 409 -> ResponseEntity.status(409).body(null);
                default -> ResponseEntity.status(500).body(null);
            };
        }
    }
}
