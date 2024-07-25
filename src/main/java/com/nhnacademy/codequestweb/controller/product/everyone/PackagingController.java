package com.nhnacademy.codequestweb.controller.product.everyone;

import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import com.nhnacademy.codequestweb.service.product.PackagingService;
import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product/packaging")
public class PackagingController {
    private final PackagingService packagingService;

    @GetMapping("/page")
    public String getPackagingPage(
            @RequestParam(required = false, name = "productState") Integer productState,
            @RequestParam(required = false, name = "page") Integer page,
            RedirectAttributes redirectAttributes,
            Model model) {
        page = page == null ? 1 : page;
        try {
            ResponseEntity<Page<PackagingGetResponseDto>> response = packagingService.getPackagingPage(productState, page, 9);
            Page<PackagingGetResponseDto> responsePage = response.getBody();
            if (responsePage != null) {
                model.addAttribute("packagingList", responsePage.getContent());
                model.addAttribute("page", page);
                model.addAttribute("totalPage", responsePage.getTotalPages());
                model.addAttribute("url", "?");
                model.addAttribute("mainText", "포장지");
                model.addAttribute("view", "packagingListPage");
                return "index";
            }else {
                redirectAttributes.addFlashAttribute("alterMessage", "응답 본문이 비어있습니다.");
                return "redirect:/";
            }
        }catch (FeignException e) {
            log.warn("error occurred while getting packaging list, message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("alterMessage", "포장지 조회에 실패했습니다.");
            return "redirect:/";
        }
    }
}
