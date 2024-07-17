package com.nhnacademy.codequestweb.controller.product.adminOnly;

import com.nhnacademy.codequestweb.request.product.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.packaging.PackagingUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import com.nhnacademy.codequestweb.service.image.ImageService;
import com.nhnacademy.codequestweb.service.product.PackagingService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/packaging")
public class AdminPackagingController {
    private final PackagingService packagingService;
    private final ImageService imageService;

    @GetMapping("/all")
    public String getPackagingList(
            @RequestParam(required = false, name = "productState") Integer productState,
            Model model) {
        ResponseEntity<List<PackagingGetResponseDto>> response = packagingService.getPackagingList(productState);
        return "index";
    }

    @GetMapping("/page")
    public String getPackagingPage(
            @RequestParam(required = false, name = "productState") Integer productState,
            @RequestParam(required = false, name = "page") Integer page,
            Model model) {
        page = page == null ? 1 : page;
        ResponseEntity<Page<PackagingGetResponseDto>> response = packagingService.getPackagingPage(productState, page, 10);
        model.addAttribute("packagingList", response.getBody().getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("view", "adminPage");
        model.addAttribute("adminPage", "packagingListPage");
        model.addAttribute("url", productState);
        return "index";
    }

    @GetMapping("/registerForm")
    public String registerForm(HttpServletRequest req) {
//        model.addAttribute("action", "register");
        req.setAttribute("action", "register");
        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "packagingRegisterForm");
        return "index";
    }

    @GetMapping("/updateForm/{productId}")
    public String updateForm(@PathVariable Long productId, Model model) {
        ResponseEntity<PackagingGetResponseDto> response = packagingService.getPackagingByProductId(productId);
        model.addAttribute("packaging", response.getBody());
        model.addAttribute("action", "update");
        model.addAttribute("view", "adminPage");
        model.addAttribute("adminPage", "packagingRegisterForm");
        return "index";
    }

    @PostMapping("/register")
    public String savePackaging(
            HttpServletRequest req,
            @RequestParam("image") MultipartFile file,
            @ModelAttribute PackagingRegisterRequestDto requestDto,
            Model model){
        String imageUrl = imageService.uploadImage(file);
        requestDto.setProductThumbnailUrl(imageUrl);
        log.info("request : {}", requestDto.toString());
        ResponseEntity<ProductRegisterResponseDto> response = packagingService.savePackaging(CookieUtils.setHeader(req), requestDto);

        return "index";
    }

    @PutMapping("/update")
    public String updatePackaging(
            HttpServletRequest req,
            @RequestParam(name = "image", required = false) MultipartFile file,
            @ModelAttribute PackagingUpdateRequestDto requestDto,
            Model model){
        if (!file.isEmpty()){
            log.info("file : {}", file.getOriginalFilename());
            String imageUrl = imageService.uploadImage(file);
            requestDto.setProductThumbnailUrl(imageUrl);
        }
        ResponseEntity<ProductUpdateResponseDto> response = packagingService.updatePackaging(CookieUtils.setHeader(req), requestDto);

        return "index";
    }
}
