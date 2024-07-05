//package com.nhnacademy.codequestweb.controller.payment;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.json.simple.parser.ParseException;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//
///**
// * 결제 관련 컨트롤러의 메서드들을 정의하는 인터페이스입니다.
// *
// * @author 김채호
// * @version 1.0
// */
//public interface PaymentController {
//
//    /**
//     * 주문 ID를 받아 결제 페이지로 이동하는 메서드입니다. 사용자가 결제하기 버튼을 눌렀을 때 페이지를 보여 줍니다. 토스 페이먼츠에서 제공하는 결제 창에 관련 정보들을
//     * 띄우는 데에 주문 관련된 정보들이 필요합니다.
//     *
//     * @param orderId 주문 ID
//     * @param model   모델 객체
//     * @return 결제 페이지의 뷰 이름
//     */
//    @GetMapping("/client/order/{orderId}/payment")
//    String savePayment(@PathVariable long orderId, Model model, HttpServletRequest req);
//
//    /**
//     * 결제 성공 시 결과 페이지로 이동하는 메서드입니다. 사용자가 결제 창에서 올바른 정보를 입력했다면 토스 페이먼츠에서 해당 URL 로 이동합니다. 토스 페이먼츠는
//     * Query Parameter 를 이용해서 여러 값을 넘겨 줍니다. 1) 이 메서드에서는 해당 값들을 사용자가 조작하지는 않았는지 검증하는 절차를 거칩니다. 2) 또한,
//     * 결제가 최종적으로 성공했을 때 토스 페이먼츠에 승인 요청을 보냅니다. 3) 승인∂ 요청을 보낸 이후 받은 응답을 파싱하여 사용자에게 결과를 보여 줍니다.
//     *
//     * @param orderId     주문 ID
//     * @param model       모델 객체
//     * @param tossOrderId 토스에서 받은 주문 ID (토스 결제창 제공)
//     * @param amount      결제 금액 (토스 결제창 제공)
//     * @param paymentKey  결제 키 (토스 결제창 제공)
//     * @return 결제 성공 페이지의 뷰 이름
//     * @throws ParseException 토스 페이먼츠 승인 응답을 파싱하는 과정 중에 발생할 수 있는 예외.
//     */
//    @GetMapping("/client/order/{orderId}/payment/success")
//    String paymentResult(
//        @PathVariable long orderId, Model model,
//        @RequestParam(value = "orderId") String tossOrderId,
//        @RequestParam(value = "amount") long amount,
//        @RequestParam(value = "paymentKey") String paymentKey) throws ParseException;
//
//    /**
//     * 결제 실패 시 결과 페이지로 이동하는 메서드입니다. 실패하는 경우는 다음 URL 에서 참고할 수 있습니다.
//     * https://docs.tosspayments.com/reference/error-codes#%EA%B2%B0%EC%A0%9C-%EC%8A%B9%EC%9D%B8
//     *
//     * @param orderId 주문 ID
//     * @param model   모델 객체
//     * @param message 결제 실패 메시지 (토스 페이먼츠 제공)
//     * @param code    결제 실패 코드 (토스 페이먼츠 제공)
//     * @return 결제 실패 페이지의 뷰 이름
//     */
//    @GetMapping("/client/order/{orderId}/payment/fail")
//    String paymentResult(
//        @PathVariable long orderId,
//        Model model,
//        @RequestParam(value = "message") String message,
//        @RequestParam(value = "code") Integer code);
//}