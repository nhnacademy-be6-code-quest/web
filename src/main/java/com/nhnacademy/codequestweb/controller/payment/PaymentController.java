package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.response.payment.OrderPaymentResponseDto;
import com.nhnacademy.codequestweb.response.payment.ProductOrderDetailResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/client/order/payment")
    public String savePayment(/* TODO: 나중에 주석 풀기 @PathVariable long orderId,*/ Model model) {

        ProductOrderDetailResponseDto productOrderDetailResponseDto = ProductOrderDetailResponseDto.builder()
            .productId(1L)
            .quantity(3L)
            .pricePerProduct(10000L)
            .productName("10000 원 짜리 테스트 상품")
            .build();

        ProductOrderDetailResponseDto productOrderDetailResponseDto2 = ProductOrderDetailResponseDto.builder()
            .productId(2L)
            .quantity(2L)
            .pricePerProduct(20000L)
            .productName("20000 원 짜리 테스트 상품")
            .build();

        ProductOrderDetailResponseDto productOrderDetailResponseDto3 = ProductOrderDetailResponseDto.builder()
            .productId(3L)
            .quantity(1L)
            .pricePerProduct(30000L)
            .productName("30000 원 짜리 테스트 상품")
            .build();

        List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList = new ArrayList<>();
        productOrderDetailResponseDtoList.add(productOrderDetailResponseDto);
        productOrderDetailResponseDtoList.add(productOrderDetailResponseDto2);
        productOrderDetailResponseDtoList.add(productOrderDetailResponseDto3);

//        OrderPaymentResponseDto orderPaymentResponseDto = paymentService.findOrderPaymentResponseDtoByOrderId(orderId);
        OrderPaymentResponseDto orderPaymentResponseDto = OrderPaymentResponseDto.builder()
            .orderTotalAmount(100000L)
            .discountAmountByCoupon(20000L)
            .discountAmountByPoint(20000L)
            .tossOrderId(UUID.randomUUID().toString())
            .productOrderDetailResponseDtoList(productOrderDetailResponseDtoList)
            .build();

        model.addAttribute("payAmount", orderPaymentResponseDto.getOrderTotalAmount()
            - orderPaymentResponseDto.getDiscountAmountByCoupon()
            - orderPaymentResponseDto.getDiscountAmountByCoupon());

        model.addAttribute("tossOrderId", orderPaymentResponseDto.getTossOrderId());

        model.addAttribute("orderName",
            orderPaymentResponseDto.getProductOrderDetailResponseDtoList().getFirst()
                .getProductName() + " 외 " + (
                orderPaymentResponseDto.getProductOrderDetailResponseDtoList().size() - 1) + "건");

//        String customerName = paymentService.getCustomerNameByOrderId(orderId);
        String customerName = "김채호";
        model.addAttribute("customerName", customerName);

        model.addAttribute("successUrl", "https://localhost:8080/client/order/payment/success");

        model.addAttribute("failUrl", "https://localhost:8080/client/order/payment/fail");

        return "/view/payment/tossPage";
    }

    @GetMapping("/client/order/payment/success")
    public String successPayment() {
        return "view/payment/success";
    }

    @GetMapping("/client/order/payment/fail")
    public String failedPayment() {
        return "view/payment/failed";
    }

    // 사용자에게 결제와 관련된 정보를 입력 받습니다.
    @PostMapping("client/order/payment")
    public void savePayment(/* TODO: 나중에 주석 풀기 @PathVariable long orderId */) {

    }
}