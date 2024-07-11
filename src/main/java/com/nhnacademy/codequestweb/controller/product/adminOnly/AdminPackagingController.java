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
    public String all(
            @RequestParam(required = false, name = "productState") Integer productState,
            Model model) {
        ResponseEntity<List<PackagingGetResponseDto>> response = packagingService.getPackaging(productState);
        return "index";
    }

    @GetMapping("/registerForm")
    public String registerForm(Model model) {
        model.addAttribute("action", "register");

        return "view/product/packagingRegisterForm";
    }

    @GetMapping("/updateForm/{productId}")
    public String updateForm(@PathVariable Long productId, Model model) {
        ResponseEntity<PackagingGetResponseDto> response = packagingService.getPackagingByProductId(productId);
        model.addAttribute("packaging", response.getBody());
        model.addAttribute("action", "update");

        return "view/product/packagingRegisterForm";
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
