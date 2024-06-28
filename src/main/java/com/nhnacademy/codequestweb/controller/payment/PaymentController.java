package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import com.nhnacademy.codequestweb.response.payment.OrderPaymentResponseDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final CouponService couponService;
    private final OrderService orderService; // ResponseDto -> 주문 총 금액
//    private final PointService pointService;
//    private final ClientService clientService;

    private static final String ID_HEADER = "X-User-Id";

    // 사용자에게 결제와 관련된 정보를 보여줍니다.
    @GetMapping("/client/order/{orderId}/payment")
    public String createPayment(@PathVariable long orderId, Model model, HttpServletRequest httpServletRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(httpServletRequest, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(httpServletRequest, "refresh"));

        List<CouponResponseDto> coupons = couponService.findClientCoupon(headers);
        model.addAttribute("coupons", coupons);

//        포인트에서 받아 올 것
        Long remainingPoint = 10000L;
//        Long remainingPoint = pointService.findByClientId(headers);
        model.addAttribute("remainingPoint", remainingPoint);

//        주문에서 받아 올 것 : totalPrice + List<ProductOrderDetailResponseDto>
        // TODO : 추가적으로 구현해야 함.
        OrderPaymentResponseDto orderPaymentResponseDto = paymentService.findOrderPaymentResponseDtoByOrderId(orderId);
        model.addAttribute("orderPaymentResponseDto", orderPaymentResponseDto);

        /*
            내가 표현해야 할 것
                - 1. 주문한 금액의 총량
                : 주문한 금액의 총량을 표현하기 위해서는 orderId 를 통해서 totalPrice 만 가져오면 됨.

                - 2. 총 결제 금액
                : 계산해서 항상 바뀌는 금액이기 때문에, 여러 가지를 고려해야 함

                내가 가지고 있는 것
                : @PathVariable long orderId

                orderId를 통해 가져 와야 할 것.
                : totalPrice (총 주문 금액), List<ProductOrderDetailDto> (상품 주문 상세)

                ProductOrderDetailDto 에 필요한 것
                : productId (특정 상품 쿠폰 적용 위해), quantity, pricePerProduct, <상품 쪽에 API 요청해야 함>productCategoryId (특정 카테고리 쿠폰 적용 위해)

                    - 2-1. 쿠폰 관련 정보 가져오기

                    CouponResponseDto 에 필요한 것
                    - couponId : 쿠폰 소모에 사용하기 위함
                    - couponType : 웰컴, 생일, 도서, 카테고리 등을 표시하기 위함
                    - couponPolicy : 여기 있는 거 죄다 사용함. (productId, productCategoryId, discountType, discountValue 등)
                    - expirationDate : 사용하는 동안에 만기일이 넘어갈 수 있으니 포함
                    - status : AVAILABLE 한 쿠폰만 가져오기 위함.

                    - 2-2. 쿠폰 사용하기

                    1) 상품 아이디에 사용할 수 있는 쿠폰

                    2) 카테고리에 사용할 수 있는 쿠폰

                    3) 그 외 웰컴 쿠폰 혹은 생일 쿠폰 (쿠폰 정책에 따라 개별로 만들어서 사용)

                - 주문 아이디를 통해 주문 상세를 통해서 다 가져와야 함.
                - 쿠폰을 적용해야 하기 때문에, 순수하게 포장지가 아닌 금액을 가져와야 함. -> 상품주문상세

                - 주문을 조회할 때,
                - 상품과 포장지가 관계를 가져야 하기 때문에, 상품 주문 상세 옵션을 만들었음. (포장지나 사은품 같은 것들)

                - 쿠폰을 적용해야 하기 때문에, 도서의 아이디와 카테고리가 필요함.
         */


        // 포인트 정책하고 같이 생각할 것. (일단 10퍼센트로 생각)
        model.addAttribute("expectedPoints", 1800);
        return "/view/payment/createPayment";
    }

    // 사용자에게 결제와 관련된 정보를 입력 받습니다.
    @PostMapping("client/order/{orderId}/payment")
    public String createPayment(@ModelAttribute PaymentRequestDto paymentRequestDto) {
//        paymentRequestDto.setOrderId(1L); // hidden input 으로 가져 왔음.
        paymentService.savePayment(paymentRequestDto);
        return "redirect:/client/order/payment/tossPayment";
    }

    // test
    @GetMapping("/client/order/{orderId}/payment/tossPayment")
    public String tossPayment() {
        return "/view/payment/tossPayment";
    }

    // 결제 정보를 단일로 조회할 수 있습니다.
    @GetMapping("/client/order/{orderId}/payment/{paymentId}")
    @ResponseBody
    public String findPaymentByPaymentId(@PathVariable Long paymentId, Model model) {
        ResponseEntity<PaymentResponseDto> paymentResponseDtoResponseEntity = paymentService.findByPaymentId(paymentId);
        model.addAttribute("paymentResponseDtoResponseEntity", paymentResponseDtoResponseEntity);
        return paymentResponseDtoResponseEntity.getBody().toString();
    }
}