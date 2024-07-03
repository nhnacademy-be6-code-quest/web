package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderValidationRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import com.nhnacademy.codequestweb.response.payment.ProductOrderDetailResponseDto;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final String secretKey;

    @GetMapping("/client/order/{orderId}/payment")
    public String savePayment(@PathVariable long orderId, Model model) {

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
        PaymentOrderRequestDto paymentOrderRequestDto = PaymentOrderRequestDto.builder()
            .orderTotalAmount(100000L)
            .discountAmountByCoupon(20000L)
            .discountAmountByPoint(20000L)
            .tossOrderId(/* TODO: 수정 필요. UUID.randomUUID().toString()*/ "04b4e96f-1a48-40fb-8ce6-16e10ab54c80")
            .productOrderDetailResponseDtoList(productOrderDetailResponseDtoList)
            .build();

        model.addAttribute("payAmount", paymentOrderRequestDto.getOrderTotalAmount()
            - paymentOrderRequestDto.getDiscountAmountByCoupon()
            - paymentOrderRequestDto.getDiscountAmountByCoupon());

        model.addAttribute("tossOrderId", paymentOrderRequestDto.getTossOrderId());

        model.addAttribute("orderName",
            paymentOrderRequestDto.getProductOrderDetailResponseDtoList().getFirst()
                .getProductName() + " 외 " + (
                paymentOrderRequestDto.getProductOrderDetailResponseDtoList().size() - 1) + "건");

//        String customerName = paymentService.getCustomerNameByOrderId(orderId);
        String customerName = "김채호";
        model.addAttribute("customerName", customerName);

        model.addAttribute("successUrl",
            "https://localhost:8080/client/order/" + orderId + "/payment/success");

        model.addAttribute("failUrl",
            "https://localhost:8080/client/order/" + orderId + "/payment/fail");

        return "/view/payment/tossPage";
    }

    @GetMapping("/client/order/{orderId}/payment/success")
    public String paymentResult(
        @PathVariable long orderId,
        Model model,
        @RequestParam(value = "orderId") String tossOrderId, // 토스를 위해 넣어 준 orderId
        @RequestParam(value = "amount") long amount, // 토스가 말하는 결제 금액
        @RequestParam(value = "paymentKey") String paymentKey) throws Exception {

//        주문 정보 검증
//        PaymentOrderPriceDto paymentOrderPriceDto = orderService.findPaymentOrderPriceDtoByOrderId(
//            orderId);

        PaymentOrderValidationRequestDto paymentOrderValidationRequestDto = PaymentOrderValidationRequestDto.builder()
            .orderTotalAmount(100000L)
            .discountAmountByCoupon(20000L)
            .discountAmountByPoint(20000L)
            .tossOrderId("04b4e96f-1a48-40fb-8ce6-16e10ab54c80")
            .build();

        // TODO 1. OrderService 에서 넘겨 받은 총 결제 금액과, 토스의 amount 가 일치하는지 확인.
        if (paymentOrderValidationRequestDto == null
            || (paymentOrderValidationRequestDto.getOrderTotalAmount()
            - paymentOrderValidationRequestDto.getDiscountAmountByPoint()
            - paymentOrderValidationRequestDto.getDiscountAmountByCoupon()) != amount

            // TODO 2. OrderService 에서 넘긴 (토스)orderId 와 Query Parameter 의 orderId 가 일치하는지 확인
            || (!paymentOrderValidationRequestDto.getTossOrderId().equals("04b4e96f-1a48-40fb-8ce6-16e10ab54c80"))) {

            // 주문 정보가 일치하지 않으면 에러 처리
            model.addAttribute("isSuccess", false);
            model.addAttribute("code", "INVALID_ORDER");
            model.addAttribute("message", "주문 정보가 일치하지 않습니다.");
            return "view/payment/failed";
        }

        // TODO 2. 토스 페이먼트에 실제로 결제를 요청함.
        // 시크릿 키 설정 (주의: 실제 환경에서는 안전하게 관리해야 함)
        // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        // 토스페이먼츠 결제 승인 API 엔드포인트 URL 생성
        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        // HTTP 연결 설정
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // 요청 본문 데이터 생성
        JSONObject obj = new JSONObject();
        obj.put("orderId", tossOrderId);
        obj.put("amount", amount);

        // 요청 전송
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        // 응답 코드 확인 및 성공 여부 판단
        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;
        model.addAttribute("isSuccess", isSuccess);

        // 응답 데이터 읽기
        InputStream responseStream =
            isSuccess ? connection.getInputStream() : connection.getErrorStream();
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        reader.close();

        if (isSuccess) {
            Object amountObj = jsonObject.get("totalAmount");
            String responseOrderId = (String) jsonObject.get("orderId");
            long responseAmount = ((Number) amountObj).longValue();
        }

        // 응답 데이터를 모델에 추가 및 콘솔 출력
        model.addAttribute("responseStr", jsonObject.toJSONString());
        System.out.println(jsonObject.toJSONString());

        String methodName = (String) jsonObject.get("method");

        // 결제 방식과 주문명을 모델에 추가
        model.addAttribute("method", (String) jsonObject.get("method"));
        model.addAttribute("orderName", (String) jsonObject.get("orderName"));


        // 결제 방식에 따른 추가 정보 처리
        if (((String) jsonObject.get("method")) != null) {
            switch ((String) jsonObject.get("method")) {
                case "카드":
                    model.addAttribute("cardNumber",
                        (String) ((JSONObject) jsonObject.get("card")).get("number"));
                    break;
                case "가상계좌":
                    model.addAttribute("accountNumber",
                        (String) ((JSONObject) jsonObject.get("virtualAccount")).get(
                            "accountNumber"));
                    break;
                case "계좌이체":
                    model.addAttribute("bank",
                        (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
                    break;
                case "휴대폰":
                    model.addAttribute("customerMobilePhone",
                        (String) ((JSONObject) jsonObject.get("mobilePhone")).get(
                            "customerMobilePhone"));
                    break;
            }
        } else {
            // 결제 방식이 없는 경우 (에러 상황) 에러 정보를 모델에 추가
            model.addAttribute("code", (String) jsonObject.get("code"));
            model.addAttribute("message", (String) jsonObject.get("message"));
        }

        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
            .orderId(orderId)
            .payAmount(amount)
            .paymentMethodName(methodName)
            .tossPaymentKey(paymentKey)
            .build();

        // 결제 성공 페이지로 이동
        paymentService.savePayment(orderId, paymentResponseDto);
        return "view/payment/success";
    }

    @GetMapping("/client/order/payment/fail")
    public String paymentResult(
        Model model,
        @RequestParam(value = "message") String message,
        @RequestParam(value = "code") Integer code) {
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        return "view/payment/failed";
    }

    // 사용자에게 결제와 관련된 정보를 입력 받습니다.
    @PostMapping("client/order/payment")
    public void savePayment(/* TODO: 나중에 주석 풀기 @PathVariable long orderId */) {

    }
}