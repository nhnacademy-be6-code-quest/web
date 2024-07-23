package com.nhnacademy.codequestweb.controller.product.admin;

import com.nhnacademy.codequestweb.request.product.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.packaging.PackagingUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import com.nhnacademy.codequestweb.service.image.ImageService;
import com.nhnacademy.codequestweb.service.product.PackagingService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/packaging")
public class AdminPackagingController {
    private final PackagingService packagingService;
    private final ImageService imageService;

    private static final String ALTER_MESSAGE = "alterMessage";

    private static final String ADMIN_PAGE = "adminPage";

    private static final String INDEX = "index";

    private static final String REDIRECT_ADMIN_MAIN = "redirect:/admin/client/0";


    @GetMapping("/page")
    public String getPackagingPage(
            @RequestParam(required = false, name = "productState") Integer productState,
            @RequestParam(required = false, name = "page") Integer page,
            RedirectAttributes redirectAttributes,
            Model model) {
        page = page == null ? 1 : page;
        try {
            ResponseEntity<Page<PackagingGetResponseDto>> response = packagingService.getPackagingPage(productState, page, 10);
            Page<PackagingGetResponseDto> responsePage = response.getBody();
            if (responsePage != null) {
                model.addAttribute("packagingList", responsePage.getContent());
                model.addAttribute("page", page);
                model.addAttribute("totalPage", responsePage.getTotalPages());
                model.addAttribute("view", ADMIN_PAGE);
                model.addAttribute(ADMIN_PAGE, "packagingListPage");
                model.addAttribute("url", "?");
                return INDEX;
            }else {
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "응답 본문이 비어있습니다.");
                return REDIRECT_ADMIN_MAIN;
            }
        }catch (FeignException e) {
            log.warn("error occurred while getting packaging list, message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "포장지 조회에 실패했습니다.");
            return REDIRECT_ADMIN_MAIN;
        }
    }

    @GetMapping("/registerForm")
    public String registerForm(HttpServletRequest req) {
        req.setAttribute("action", "register");
        req.setAttribute("view", ADMIN_PAGE);
        req.setAttribute(ADMIN_PAGE, "packagingRegisterForm");
        return INDEX;
    }

    @GetMapping("/updateForm/{productId}")
    public String updateForm(@PathVariable Long productId, RedirectAttributes redirectAttributes, Model model) {
        try {
            ResponseEntity<PackagingGetResponseDto> response = packagingService.getPackagingByProductId(productId);
            PackagingGetResponseDto responseDto = response.getBody();
            if (responseDto != null) {
                model.addAttribute("packaging", response.getBody());
                model.addAttribute("action", "update");
                model.addAttribute("view", ADMIN_PAGE);
                model.addAttribute(ADMIN_PAGE, "packagingRegisterForm");
                return INDEX;
            }else {
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "응답 본문이 비어있습니다.");
                return REDIRECT_ADMIN_MAIN;
            }
        }catch (FeignException e) {
            log.warn("error occurred while getting packaging with id {}, message : {}", productId, e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "포장지 조회에 실패했습니다.");
            return REDIRECT_ADMIN_MAIN;
        }
    }


    @PostMapping("/register")
    public String savePackaging(
            HttpServletRequest req,
            @RequestParam("image") MultipartFile file,
            @ModelAttribute PackagingRegisterRequestDto requestDto,
            RedirectAttributes redirectAttributes){
        boolean imageSaved = true;
        if (file != null && !file.isEmpty()){
            imageSaved = false;
            try {
                String imageUrl = imageService.uploadImage(file);
                requestDto.setProductThumbnailUrl(imageUrl);
                imageSaved = true;
            }catch (FeignException e){
                log.warn("error occurred while saving image for save new packaging. message : {}", e.getMessage());
            }
        }
        try {
            ResponseEntity<ProductRegisterResponseDto> responseEntity = packagingService.savePackaging(CookieUtils.setHeader(req), requestDto);
            if (responseEntity.getStatusCode().is2xxSuccessful()){
                if (imageSaved){
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "성공적으로 등록되었습니다.");
                }else {
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "포장지는 등록되었지만, 포장지 이미지를 저장하는 과정에서 문제가 발생했습니다.\n 자세한 정보는 로그를 확인하세요.");
                }
            }
        }catch (FeignException e){
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "포장지를 등록하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요.");
            log.warn("error occurred while registering packaging product : {}", e.getMessage());
        }

        return "redirect:/admin/product/packaging/page";
    }

    @PutMapping("/update")
    public String updatePackaging(
            HttpServletRequest req,
            @RequestParam(name = "image", required = false) MultipartFile file,
            @ModelAttribute PackagingUpdateRequestDto requestDto,
            RedirectAttributes redirectAttributes){
        boolean imageSaved = true;
        if (file != null && !file.isEmpty()){
            imageSaved = false;
            try {
                String imageUrl = imageService.uploadImage(file);
                requestDto.setProductThumbnailUrl(imageUrl);
                imageSaved = true;
            }catch (FeignException e){
                log.warn("error occurred while saving image for update packaging. message : {}", e.getMessage());
            }
        }
        try {
            ResponseEntity<ProductUpdateResponseDto> responseEntity = packagingService.updatePackaging(CookieUtils.setHeader(req), requestDto);
            if (responseEntity.getStatusCode().is2xxSuccessful()){
                if (imageSaved){
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "성공적으로 수정되었습니다.");
                }else {
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "수정은 이루어졌지만, 포장지 이미지를 저장하는 과정에서 문제가 발생했습니다.\n 자세한 정보는 로그를 확인하세요.");
                }
            }
        }catch (FeignException e){
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "포장지를 수정하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요.");
            log.warn("error occurred while updating packaging product : {}", e.getMessage());
        }
        return "redirect:/admin/product/packaging/page";
    }
}
