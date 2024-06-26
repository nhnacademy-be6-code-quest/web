package com.nhnacademy.codequestweb.controller.order;

import com.nhnacademy.codequestweb.request.order.ClientViewOrderPostRequestDto;
import com.nhnacademy.codequestweb.request.order.client.ClientOrderPostRequestDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderPostResponseDto;
import com.nhnacademy.codequestweb.response.order.client.ClientViewOrderPostResponseDto;
import com.nhnacademy.codequestweb.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @GetMapping("/views/order")
    // 뷰 화면 테스트용 api입니다. 실제 서비스에서는 하위의 POST api 사용예정입니다.
    public String order(Model model, HttpServletRequest request) {
        ClientViewOrderPostRequestDto orderRequestDto = new ClientViewOrderPostRequestDto(new ArrayList<>());
        ResponseEntity<ClientViewOrderPostResponseDto> response = orderService.viewOrder(orderRequestDto);
        model.addAttribute("orderResponseDto", response.getBody());
        model.addAttribute("clientId", 1L);
        return "view/order/order";
    }

//    @PostMapping("/views/order")
//    public String orderView(ClientOrderPostRequestDto orderRequestDto, Model model){
//        ResponseEntity<ClientOrderPostResponseDto> response = orderService.gotoOrder(orderRequestDto);
//        model.addAttribute("orderResponseDto", response.getBody());
//        return "view/order/order";
//    }

    @ResponseBody
    @PostMapping("/client/order")
    public long createOrder(@RequestBody ClientOrderPostRequestDto clientOrderPostRequestDto, RedirectAttributes redirectAttributes) throws IOException { // order 생성 요청
        return orderService.createOrder(clientOrderPostRequestDto);
    }

}
