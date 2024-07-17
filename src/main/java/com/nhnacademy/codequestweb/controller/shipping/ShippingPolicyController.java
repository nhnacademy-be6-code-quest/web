package com.nhnacademy.codequestweb.controller.shipping;

import com.nhnacademy.codequestweb.request.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import com.nhnacademy.codequestweb.service.order.AdminOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShippingPolicyController {

    private final AdminOrderService adminOrderService;

    @PostMapping("/admin/shipping-policy/update") //
    public String updateShippingPolicy(HttpServletRequest request, @ModelAttribute AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto) {
        adminOrderService.updateShippingPolicy(request, adminShippingPolicyPutRequestDto);

        List<ShippingPolicyGetResponseDto> shippingPolicyGetResponseDtoList = adminOrderService.getShippingPolicies(request);
        request.setAttribute("shippingPolicyList", shippingPolicyGetResponseDtoList);

        request.setAttribute("view", "adminPage");
        request.setAttribute("adminPage", "adminShippingPolicy");
        request.setAttribute("activeSection", "order");
        return "index";
    }

}
