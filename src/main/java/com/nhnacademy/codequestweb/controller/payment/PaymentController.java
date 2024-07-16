package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.ClientUpdateGradeRequestDto;
import com.nhnacademy.codequestweb.request.payment.CouponPaymentRewardRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentAccumulatePointRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentCompletedCouponRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderShowRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentUsePointRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentGradeResponseDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    // PaymentController 의 Service 는 PaymentService 만 있어야 함. 단일 책임의 원칙...
    // 하나의 컨트롤러가 여러 개의 서비스를 호출하면 1) 테스트 어렵고 2) 확장성도 떨어짐
    private final PaymentService paymentService;

    @GetMapping("/client/order/{orderId}/payment")
    public String savePayment(@RequestHeader HttpHeaders headers, @PathVariable long orderId,
        Model model) {
//        1. 주문에서 받은 값을 토대로 사용자에게 보여 주기
        PaymentOrderShowRequestDto paymentOrderShowRequestDto = paymentService.findPaymentOrderShowRequestDtoByOrderId(
            headers, orderId);
        model.addAttribute("paymentOrderShowRequestDto", paymentOrderShowRequestDto);
        model.addAttribute("successUrl",
            "https://localhost:8080/client/order/" + orderId + "/payment/success");
        model.addAttribute("failUrl",
            "https://localhost:8080/client/order/" + orderId + "/payment/fail");
        return "view/payment/tossPage";
    }

    @GetMapping("/client/order/{orderId}/payment/success")
    public String paymentResult(HttpServletRequest request,
        @PathVariable long orderId, Model model,
        @RequestParam(value = "orderId") String tossOrderId,
        @RequestParam long amount, @RequestParam String paymentKey) throws ParseException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(request, "access"));

//        2. 결제 검증 및 승인 창에서 필요한 요소를 Order 에서 받아 오기 : @RequestHeader 로 해결함. // 303 ->
        PaymentOrderApproveRequestDto paymentOrderApproveRequestDto = paymentService.findPaymentOrderApproveRequestDtoByOrderId(
            headers, orderId);

//        3. 조작 확인하기 : 주문 정보가 일치하지 않으면 실패 페이지로 이동하기.
        if (!paymentService.isValidTossPayment(paymentOrderApproveRequestDto, tossOrderId,
            amount)) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("code", "INVALID_ORDER");
            model.addAttribute("message", "주문 정보가 일치하지 않습니다.");
            log.warn("주문 아이디 : {} 에서 결제 조작이 의심됩니다.", orderId);
            return "view/payment/failed";
        }

        // 4. 재고 없으면 재고 없다고 하고, 결제 fail URL 로 이동하게 만들기
        boolean isProductInventoryReducedNormally = true;

        try {
            paymentService.decreaseProductInventory(
                paymentOrderApproveRequestDto.getProductOrderDetailList());
        } catch (FeignException e) {
            log.error("재고 감소 처리 중 상품 서버에서 에러가 발생했습니다. 재고 부족으로 인한 오류일 수 있습니다.\norderId: {}",
                orderId);
            model.addAttribute("isSuccess", false);
            model.addAttribute("code", "NO_PRODUCT_INVENTORY");
            model.addAttribute("message",
                "상품 관련 서비스 에러입니다. 재고 부족으로 인한 오류일 수 있으므로, 결제는 진행되지 않았습니다.");
            return "view/payment/failed";
        } catch (Exception e) {
            log.error("재고 감소 처리 중 알 수 없는 에러가 발생했습니다.\nproductOrderDetailList: {}\nerrorMessage: {}",
                paymentOrderApproveRequestDto.getProductOrderDetailList(),
                e.getMessage());
            isProductInventoryReducedNormally = false;
        }

        // 5. 쿠폰 사용, 포인트 사용, 포인트 적립, 주문 상태 바꾸기

        // 5-1) 쿠폰 사용하기
        boolean isCouponProcessedNormally = true;

        try {
            if (paymentOrderApproveRequestDto.getCouponId() != null) {
                ResponseEntity<String> couponRes = paymentService.useCoupon(headers,
                    PaymentCompletedCouponRequestDto.builder()
                        .couponId(paymentOrderApproveRequestDto.getCouponId())
                        .build());

                HttpStatusCode code = couponRes.getStatusCode();
                boolean couponUseStatusCodeIs200 = code.equals(HttpStatusCode.valueOf(200));

                if (!couponUseStatusCodeIs200) {
                    log.error("사용한 쿠폰에 대한 응답이 200이 아닙니다.\n쿠폰 아이디: {}",
                        paymentOrderApproveRequestDto.getCouponId());
                    isCouponProcessedNormally = false;
                }
            }
        } catch (FeignException fe) {
            log.error("쿠폰 사용 처리 중 쿠폰 서버에서 에러가 발생했습니다.\ncouponId: {}\nclientId: {}",
                paymentOrderApproveRequestDto.getCouponId(),
                paymentOrderApproveRequestDto.getClientId());
            isCouponProcessedNormally = false;
        } catch (Exception e) {
            log.error("쿠폰 사용 처리 중 알 수 없는 에러가 발생했습니다.\ncouponId: {}\nclientId: {}",
                paymentOrderApproveRequestDto.getCouponId(),
                paymentOrderApproveRequestDto.getClientId());
            isCouponProcessedNormally = false;
        }

        // 5-2) 포인트 사용하기
        boolean isPointUsedNormally = true;

        try {
            if (paymentOrderApproveRequestDto.getDiscountAmountByPoint() != 0) {
                PaymentUsePointRequestDto paymentUsePointRequestDto = new PaymentUsePointRequestDto();
                paymentUsePointRequestDto.setPointUsagePayment(
                    (int) paymentOrderApproveRequestDto.getDiscountAmountByPoint());

                ResponseEntity<String> pointUseResponseEntity = paymentService.usePaymentPoint(
                    paymentUsePointRequestDto, headers);

                HttpStatusCode pointUseStatusCode = pointUseResponseEntity.getStatusCode();
                boolean pointUseStatusCodeIs200 = pointUseStatusCode.equals(
                    HttpStatusCode.valueOf(200));

                if (!pointUseStatusCodeIs200) {
                    log.error("사용한 포인트에 대한 응답이 200이 아닙니다.\nclientId: {}\ndiscountAmountByPoint: {}",
                        paymentOrderApproveRequestDto.getClientId(),
                        paymentOrderApproveRequestDto.getDiscountAmountByPoint());
                    isPointUsedNormally = false;
                }
            }
        } catch (FeignException fe) {
            log.error("포인트 사용 처리 중 포인트 서버에서 에러가 발생했습니다.\ndiscountAmountByPoint: {}\nclientId: {}",
                paymentOrderApproveRequestDto.getDiscountAmountByPoint(),
                paymentOrderApproveRequestDto.getClientId());
            isPointUsedNormally = false;
        } catch (Exception e) {
            log.error("포인트 사용 처리 중 알 수 없는 에러가 발생했습니다.\ndiscountAmountByPoint: {}\nclientId: {}",
                paymentOrderApproveRequestDto.getDiscountAmountByPoint(),
                paymentOrderApproveRequestDto.getClientId());
            isPointUsedNormally = false;
        }

        // 5-3) 포인트 적립하기
        boolean isPointAccumulatedNormally = true;

        try {
            PaymentAccumulatePointRequestDto paymentAccumulatePointRequestDto = new PaymentAccumulatePointRequestDto();
            paymentAccumulatePointRequestDto.setAccumulatedPoint(
                (int) paymentOrderApproveRequestDto.getAccumulatedPoint());

            ResponseEntity<String> pointAccumulateResponseEntity = paymentService.accumulatePoint(
                headers, paymentAccumulatePointRequestDto);

            HttpStatusCode pointAccumulateStatusCode = pointAccumulateResponseEntity.getStatusCode();
            boolean pointAccumulateStatusCodeIs200 = pointAccumulateStatusCode.equals(
                HttpStatusCode.valueOf(200));

            if (!pointAccumulateStatusCodeIs200) {
                log.error("포인트 적립에 대한 응답이 200이 아닙니다.\nclientId: {}\naccumulatedPoint: {}",
                    paymentOrderApproveRequestDto.getClientId(),
                    paymentOrderApproveRequestDto.getAccumulatedPoint());
                isPointAccumulatedNormally = false;
            }
        } catch (FeignException fe) {
            log.error("포인트 적립 처리 중 포인트 서버에서 에러가 발생했습니다.\naccumulatedPoint: {}\nclientId: {}",
                paymentOrderApproveRequestDto.getAccumulatedPoint(),
                paymentOrderApproveRequestDto.getClientId());
            isPointAccumulatedNormally = false;
        } catch (Exception e) {
            log.error("포인트 적립 처리 중 알 수 없는 에러가 발생했습니다.\naccumulatedPoint: {}\nclientId: {}",
                paymentOrderApproveRequestDto.getAccumulatedPoint(),
                paymentOrderApproveRequestDto.getClientId());
            isPointAccumulatedNormally = false;
        }

        // 5-4) 주문 상태 바꾸기
        boolean isOrderStatusChangedToCompletedPaymentNormally = true;

        try {
            paymentService.changeOrderStatusCompletePayment(orderId, "결제완료");
        } catch (FeignException fe) {
            log.error("주문 상태를 결제 대기에서 결제 완료로 바꾸는 중 주문 서버에서 오류가 발생했습니다.\norderId: {}", orderId);
            isOrderStatusChangedToCompletedPaymentNormally = false;
        } catch (Exception e) {
            log.error("주문 상태를 결제 대기에서 결제 완료로 바꾸는 중 알 수 없는 에러가 발생했습니다.\norderId: {}", orderId);
            isOrderStatusChangedToCompletedPaymentNormally = false;
        }

        // 5-5) 일정 금액 이상 주문 시 쿠폰 지급하기
        boolean isRewardCouponGivenNormally = true;
        try {
            CouponPaymentRewardRequestDto couponPaymentRewardRequestDto = CouponPaymentRewardRequestDto.builder()
                .paymentValue(paymentOrderApproveRequestDto.getOrderTotalAmount()
                    - paymentOrderApproveRequestDto.getDiscountAmountByPoint()
                    - paymentOrderApproveRequestDto.getDiscountAmountByCoupon())
                .clientId(paymentOrderApproveRequestDto.getClientId())
                .build();

            ResponseEntity<String> giveRewardCouponResponseEntity = paymentService.giveRewardCoupon(
                couponPaymentRewardRequestDto);

            HttpStatusCode giveRewardCouponStatusCode = giveRewardCouponResponseEntity.getStatusCode();
            boolean giveRewardCouponStatusCodeIs200 = giveRewardCouponStatusCode.equals(
                HttpStatusCode.valueOf(200));

            if (!giveRewardCouponStatusCodeIs200) {
                log.error("일정 금액 이상 주문 시 쿠폰을 지급하는 과정에서 응답이 200이 아닙니다.");
            }
        } catch (FeignException fe) {
            log.error(
                "결제 이후 보너스 쿠폰을 지급하는 데에 쿠폰 서비스에서 오류가 발생했습니다.\n일정 금액이 넘지 않더라도 이 로그는 출력됩니다.\nclientId: {}",
                paymentOrderApproveRequestDto.getClientId());
            isRewardCouponGivenNormally = false;
        } catch (Exception e) {
            log.error(
                "결제 이후 보너스 쿠폰을 지급하는 도중 알 수 없는 오류가 발생했습니다.\n일정 급액이 넘지 않더라도 이 로그는 출력됩니다.\nclientId: {}",
                paymentOrderApproveRequestDto.getClientId());
            isRewardCouponGivenNormally = false;
        }

        // 6. 결제 승인하기
        TossPaymentsResponseDto tossPaymentsResponseDto = paymentService.approvePayment(tossOrderId,
            amount, paymentKey);

        // 7. DB에 저장하기
        paymentService.savePayment(headers, orderId, tossPaymentsResponseDto);

        // 8. 등급 바꾸기
//        try {
//            PaymentGradeResponseDto paymentGradeResponseDto = paymentService.getPaymentRecordOfClient(
//                paymentOrderApproveRequestDto.getClientId());
//            ResponseEntity<String> updateClientGradeResponseEntity = paymentService.updateClientGrade(
//                ClientUpdateGradeRequestDto.builder()
//                    .clientId(paymentOrderApproveRequestDto.getClientId())
//                    .payment(paymentGradeResponseDto.getPaymentGradeValue())
//                    .build());
//
//            HttpStatusCode updateClientGradeResponseStatusCode = updateClientGradeResponseEntity.getStatusCode();
//            boolean updateClientGradeStatusCodeIs200 = updateClientGradeResponseStatusCode.equals(
//                HttpStatusCode.valueOf(200));
//
//            if (!updateClientGradeStatusCodeIs200) {
//                log.error("결제 이후 회원의 등급을 바꾸는 과정에서 응답이 200이 아닙니다.");
//            }
//
//        } catch (FeignException fe) {
//            log.error(
//                "회원의 결제 내역을 조회하고 등급이 바뀌는지 확인하는 중 회원 서버에서 오류가 발생했습니다. 이 로그는 회원의 등급이 바뀌지 않아도 출력됩니다.\nclientId: {}",
//                paymentOrderApproveRequestDto.getClientId());
//        } catch (Exception e) {
//            log.error(
//                "회원의 결제 내역을 조회하고 등급을 바뀌는지 확인하는 중 알 수 없는 오류가 발생하였습니다. 이 로그는 회원의 등급이 바뀌지 않아도 출력됩니다.\nclientId: {}",
//                paymentOrderApproveRequestDto.getClientId());
//        }

        // 9. View 보여주기
        boolean isClient = paymentOrderApproveRequestDto.getClientId() != null;
        model.addAttribute("isClient", isClient);
        model.addAttribute("isSuccess", true);
        model.addAttribute("orderId", orderId);
        model.addAttribute("isCouponProcessedNormally", isCouponProcessedNormally);
        model.addAttribute("isPointUsedNormally", isPointUsedNormally);
        model.addAttribute("isPointAccumulatedNormally", isPointAccumulatedNormally);
        model.addAttribute("isProductInventoryReducedNormally", isProductInventoryReducedNormally);
        model.addAttribute("isOrderStatusChangedToCompletedPaymentNormally",
            isOrderStatusChangedToCompletedPaymentNormally);
        model.addAttribute("isRewardCouponGivenNormally", isRewardCouponGivenNormally);
        model.addAttribute("tossPaymentsResponseDto", tossPaymentsResponseDto);
        return "view/payment/success";
    }

    @GetMapping("/client/order/{orderId}/payment/fail")
    public String paymentResult(
        @PathVariable long orderId, Model model, @RequestParam(value = "message") String message,
        @RequestParam(value = "code") Integer code) {
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        return "view/payment/failed";
    }
}