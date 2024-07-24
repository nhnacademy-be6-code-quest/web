package com.nhnacademy.codequestweb.controller.product.everyone;

import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import com.nhnacademy.codequestweb.service.product.PackagingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product/packaging")
public class PackagingController {
    private final PackagingService packagingService;

    // 사용자의 포장지 조회를 위해 남겨두고 추후 수정해야..
    @GetMapping("/all")
    public String all(
            Model model) {
        ResponseEntity<List<PackagingGetResponseDto>> response = packagingService.getPackagingList(0);
        model.addAttribute("pList", response.getBody());
        return "packagingListPage";
    }
}
