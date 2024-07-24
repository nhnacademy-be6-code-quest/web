package com.nhnacademy.codequestweb.controller.product.admin;

import com.nhnacademy.codequestweb.request.product.tag.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.tag.TagUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.tag.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.TagService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;
import java.util.Set;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminTagController {
    private final TagService tagService;

    private static final String REDIRECT_ADMIN_MAIN = "redirect:/admin/client/0";

    private static final String WINDOW_CLOSE = "view/product/window.close";

    private static final String ATTRIBUTE_VALUE = "태그 정보 조회에 실패했습니다.";

    private static final String ALTER_MESSAGE = "alterMessage";

    private static final String INDEX = "index";

    private String handlingResponseBody(
            ResponseEntity<Page<TagGetResponseDto>> response,
            RedirectAttributes redirectAttributes,
            Integer page,
            String sort,
            Boolean desc,
            String url,
            Model model) {

        if (response == null){
            log.warn("the response itself is null");
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "응답 자체가 비어있습니다.");
            return REDIRECT_ADMIN_MAIN;
        }else {
            Page<TagGetResponseDto> responseBody = response.getBody();
            if (responseBody == null){
                log.warn("the response body is null with response: {}", response);
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "응답 본문이 비어있습니다.");
                return REDIRECT_ADMIN_MAIN;
            }else {
                long totalElements = responseBody.getTotalElements();
                model.addAttribute("totalCount", totalElements);
                if (totalElements == 0) {
                    model.addAttribute("empty", true);
                }
                model.addAttribute("totalPage", responseBody.getTotalPages());
                model.addAttribute("page", page == null ? 1 : page);
                model.addAttribute("sort", sort);
                model.addAttribute("desc", desc);
                model.addAttribute("tagPage", responseBody);
                model.addAttribute("view", "adminPage");
                model.addAttribute("adminPage", "tags");
                model.addAttribute("url", url);
                model.addAttribute("activeSection","book");
                return INDEX;
            }
        }
    }

    @GetMapping("/admin/tags")
    public String getTags(
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            ResponseEntity<Page<TagGetResponseDto>> response = tagService.getAllTags(page, sort, desc);
            return handlingResponseBody(response, redirectAttributes, page, sort, desc, "/admin/tags?", model);
        }catch (FeignException e) {
            log.warn("error occurred while getting tags, error message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
    }

    @GetMapping("/admin/tags/containing")
    public String getNameContainingTags(
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam("tagName") String tagName,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            ResponseEntity<Page<TagGetResponseDto>> response = tagService.getNameContainingTagPage(page, sort, desc, tagName);
            return handlingResponseBody(response, redirectAttributes, page, sort, desc, "/admin/tags/containing?tagName=" + tagName + "&", model);
        }catch (FeignException e) {
            log.warn("error occurred while getting name containing tags, error message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
    }

    private String handleModalView(ResponseEntity<Page<TagGetResponseDto>> response, Model model, String url){
        if (response == null){
            log.warn("the tag response itself is null");
            return WINDOW_CLOSE;
        } else{
            Page<TagGetResponseDto> responseBody = response.getBody();
            if (responseBody == null){
                log.warn("the tag response body is null with response: {}", response);
                return WINDOW_CLOSE;
            }else {
                model.addAttribute("tagPage", responseBody);
                Set<Integer> pageNumbers = new LinkedHashSet<>();
                pageNumbers.add(1);
                for (int i = 1; i <= responseBody.getTotalPages(); i++) {
                    pageNumbers.add(i);
                }
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("url", url);

                return "view/product/tagPage";
            }
        }
    }

    //도서 등록시 불러야 함 일단 냅두기.
    @GetMapping("/tags/all")
    public String getRegisterForm(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "desc", required = false) Boolean desc,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {
        try {
            ResponseEntity<Page<TagGetResponseDto>> response = tagService.getAllTags(page, sort, desc);
            return handleModalView(response, model, "/tags/all?page=");
        }catch (FeignException e) {
            log.warn("error occurred while getting all tags modal, error message : {}", e.getMessage());
            return WINDOW_CLOSE;
        }
    }


    @GetMapping("/tags/containing")
    public String getContainingRegisterForm(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "desc", required = false) Boolean desc,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam("tagName") String tagName,
            Model model) {
        try {
            ResponseEntity<Page<TagGetResponseDto>> response = tagService.getNameContainingTagPage(page, sort, desc, tagName);
            return handleModalView(response, model, "/tags/containing?tagName="+tagName +"&page=");
        }catch (FeignException e) {
            log.warn("error occurred while getting name containing tags modal, error message : {}", e.getMessage());
            return WINDOW_CLOSE;
        }
    }

    @PostMapping("/admin/tags/register")
    public ResponseEntity<Void> saveTag(HttpServletRequest req, @ModelAttribute TagRegisterRequestDto dto) {
        try {
            ResponseEntity<TagRegisterResponseDto> response = tagService.saveTag(CookieUtils.setHeader(req), dto);
            return ResponseEntity.status(response.getStatusCode()).build();
        }catch (FeignException e){
            log.warn(e.getMessage());
            return ResponseEntity.status(e.status()).body(null);
        }
    }

    @PutMapping("/admin/tags/update")
    public ResponseEntity<Void> updateTag(HttpServletRequest req, @ModelAttribute TagUpdateRequestDto dto) {
        try {
            ResponseEntity<TagUpdateResponseDto> response = tagService.updateTag(CookieUtils.setHeader(req), dto);
            return ResponseEntity.status(response.getStatusCode()).build();
        }catch (FeignException e){
            log.warn(e.getMessage());
            return ResponseEntity.status(e.status()).body(null);
        }
    }

    @DeleteMapping("/admin/tags/delete/{tagId}")
    public ResponseEntity<Void> deleteTag(HttpServletRequest req, @PathVariable Long tagId) {
        try {
            return tagService.deleteTag(CookieUtils.setHeader(req), tagId);
        }catch (FeignException e){
            log.warn(e.getMessage());
            return ResponseEntity.status(e.status()).body(null);
        }
    }
}
