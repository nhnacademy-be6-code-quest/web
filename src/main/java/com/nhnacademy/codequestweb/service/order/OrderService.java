package com.nhnacademy.codequestweb.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.point.OrderPointClient;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterAddressRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterPhoneNumberRequestDto;
import com.nhnacademy.codequestweb.request.order.client.CouponDiscountInfoRequestDto;
import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.request.order.nonclient.FindNonClientOrderIdRequestDto;
import com.nhnacademy.codequestweb.request.order.nonclient.UpdateNonClientOrderPasswordRequestDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderGetResponseDto;
import com.nhnacademy.codequestweb.response.order.client.OrderCouponDiscountInfo;
import com.nhnacademy.codequestweb.response.order.common.OrderDetailDtoItem;
import com.nhnacademy.codequestweb.response.order.nonclient.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderGetResponseDto;
import com.nhnacademy.codequestweb.response.payment.PaymentMethodResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.ProductCategory;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.service.product.PackagingService;
import com.nhnacademy.codequestweb.service.shipping.ShippingPolicyService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private static final String INDEX = "index";
    private static final String PACKAGE_LIST = "packageList";
    private static final String SHIPPING_POLICY = "shippingPolicy";


    private final UserClient userClient;
    private final OrderClient orderClient;
    private final BookProductService bookProductService;
    private final ShippingPolicyService shippingPolicyService;
    private final OrderPointClient orderPointClient;
    private final PackagingService packagingService;
    private final PaymentService paymentService;

    public String viewClientOrder(HttpServletRequest req, Model model,
        List<String> orderItemDtoStringList) {
        log.info("회원 장바구니 주문 시도");
        // 바인딩 객체에 주문 상품 가격 정보 추가.
        List<OrderItemDto> orderItemDtoList = convertToOrderItemDtoList(orderItemDtoStringList);
        log.info("장바구니에 담은 구매할 상품: {}", orderItemDtoList);
        return processCreatingClientOrder(req, model, orderItemDtoList);
    }

    public String viewClientOrder(HttpServletRequest req, Model model, OrderItemDto orderItemDto) {
        log.info("회원 바로구매 주문 시도");
        return processCreatingClientOrder(req, model, new ArrayList<>(List.of(orderItemDto)));
    }

    public String saveClientTemporalOrder(ClientOrderForm clientOrderForm, HttpServletRequest req) {

        HttpHeaders headers = getHeader(req);

        log.info("주문 서비스 레디스에 저장할 회원 주문 데이터 dto 생성");

        clientOrderForm.setOrderTotalAmount(clientOrderForm.getProductTotalAmount() + clientOrderForm.getShippingFee());
        clientOrderForm.setOrderCode(UUID.randomUUID().toString());

        orderClient.saveClientTemporalOrder(headers, clientOrderForm);

        return clientOrderForm.getOrderCode();
    }

    public void saveNonClientTemporalOrder(HttpServletRequest request,
        NonClientOrderForm nonClientOrderForm) {
        nonClientOrderForm.setOrderCode(UUID.randomUUID().toString());
        orderClient.saveNonClientTemporalOrder(getHeader(request), nonClientOrderForm);
    }

    public String viewNonClientOrder(HttpServletRequest req, Model model,
        OrderItemDto orderItemDto) {

        log.info("비회원 단건 주문 페이지 로딩");

        HttpHeaders headers = getHeader(req);

        // ** 바인딩할 객체 **
        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        // ** 화면에 뿌릴 정보 **
        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(
            headers, "비회원배송비");

        // 책 상세 정보
        BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers,
            orderItemDto.getProductId()).getBody();

        // 폼에 추가
        assert book != null;
        nonClientOrderForm.addOrderDetailDtoItem(
            OrderDetailDtoItem.builder()
                .productId(orderItemDto.getProductId())
                .productName(book.title())
                .quantity(orderItemDto.getQuantity())
                .packableProduct(book.packable())
                .productSinglePrice(book.productPriceSales())
                .build()
        );

        model.addAttribute("view", "nonClientOrder");

        model.addAttribute("nonClientOrderForm", nonClientOrderForm);
        model.addAttribute(PACKAGE_LIST, packagingService.getPackagingList(0).getBody());
        model.addAttribute(SHIPPING_POLICY, shippingPolicy);

        return INDEX;
    }

    public String viewNonClientOrder(HttpServletRequest req, Model model,
        List<String> orderItemDtoStringList) {

        log.info("비회원 주문 시도!");

        HttpHeaders headers = getHeader(req);

        List<OrderItemDto> orderItemDtoList = convertToOrderItemDtoList(orderItemDtoStringList);

        // ** 바인딩할 객체 **
        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        // ** 화면에 뿌릴 정보 **
        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(
            headers, "비회원배송비");

        // 바인딩 객체에 주문 상품 가격 정보 추가.
        orderItemDtoList.forEach(orderItemDto -> {
            // 책 상세 정보
            BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers,
                orderItemDto.getProductId()).getBody();
            assert book != null;
            // 폼에 추가
            nonClientOrderForm.addOrderDetailDtoItem(
                OrderDetailDtoItem.builder()
                    .productId(orderItemDto.getProductId())
                    .productName(book.title())
                    .quantity(orderItemDto.getQuantity())
                    .packableProduct(book.packable())
                    .productSinglePrice(book.productPriceSales())
                    .build()
            );
        });

        List<PackagingGetResponseDto> packageList = packagingService.getPackagingList(0).getBody();

        model.addAttribute("view", "nonClientOrder");

        model.addAttribute("nonClientOrderForm", nonClientOrderForm);
        model.addAttribute(PACKAGE_LIST, packageList);
        model.addAttribute(SHIPPING_POLICY, shippingPolicy);

        return INDEX;
    }

    private List<OrderItemDto> convertToOrderItemDtoList(List<String> orderItemDtoStringList) {
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        try {
            String listString = orderItemDtoStringList.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            List<CartGetResponseDto> json2 = objectMapper.readValue(listString,
                new TypeReference<List<CartGetResponseDto>>() {
                });
            orderItemDtoList = json2.stream()
                .map(cartGetResponseDto -> OrderItemDto.builder()
                    .productId(cartGetResponseDto.productId())
                    .quantity(cartGetResponseDto.productQuantityOfCart())
                    .categoryIdList(cartGetResponseDto.categorySet().stream()
                        .map(ProductCategory::productCategoryId
                        ).toList())
                    .packable(cartGetResponseDto.packable())
                    .build())
                .toList();
        } catch (JsonProcessingException e) {
            log.debug("카테고리 목록을 orderItemDto리스트로 변환하는데 오류가 발생했습니다.");
        }
        return orderItemDtoList;
    }

    public Page<ClientOrderGetResponseDto> getClientOrders(HttpHeaders headers, int pageSize,
        int pageNo, String sortBy, String sortDir) {
        return orderClient.getClientOrders(headers, pageSize, pageNo, sortBy, sortDir).getBody();
    }

    public NonClientOrderGetResponseDto findNonClientOrder(HttpHeaders headers, long orderId,
        String orderPassword) {
        return orderClient.findNonClientOrder(headers, orderId, orderPassword).getBody();
    }

    public List<ClientDeliveryAddressResponseDto> getClientDeliveryAddressListOnOrderPage(
        HttpHeaders headers) {
        return userClient.getDeliveryAddresses(headers).getBody();
    }

    public List<ClientPhoneNumberResponseDto> getClientPhoneNumberListOnOrderPage(
        HttpHeaders headers) {
        return userClient.getPhoneNumber(headers).getBody();
    }

    public String registerPhoneNumberOnOrderPage(HttpHeaders headers,
        ClientRegisterPhoneNumberRequestDto clientRegisterPhoneNumberDto) {
        return userClient.registerPhoneNumber(headers, clientRegisterPhoneNumberDto).getBody();
    }

    public String registerAddressOnOrderPage(HttpHeaders headers,
        ClientRegisterAddressRequestDto clientRegisterAddressRequestDto) {
        return userClient.registerAddress(headers, clientRegisterAddressRequestDto).getBody();
    }

    private HttpHeaders getHeader(HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        return headers;
    }

    private String processCreatingClientOrder(HttpServletRequest request, Model model,
        List<OrderItemDto> orderItemDtoList) {

        log.info("회원 주문 정보 입력 화면 진입");

        HttpHeaders headers = getHeader(request);

        // ** 바인딩할 객체 **
        ClientOrderForm clientOrderForm = new ClientOrderForm();

        // 배송비
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(
            headers, "회원배송비");
        clientOrderForm.setShippingFee(shippingPolicy.shippingFee());

        // 결제금액 계산
        long productTotalAmount = 0L; // 상품 총 금액 미리 계산(옵션 상품 선택 전)

        // 수량
        long totalQuantity = 0;

        // 책 주문 정보
        for (OrderItemDto orderItemDto : orderItemDtoList) {

            totalQuantity += orderItemDto.getQuantity();

            // 책 상세 정보
            BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers,
                orderItemDto.getProductId()).getBody();

            assert book != null;
            productTotalAmount += book.productPriceSales();

            // 폼에 추가
            clientOrderForm.addOrderDetailDtoItem(
                OrderDetailDtoItem.builder()
                    .productId(orderItemDto.getProductId())
                    .productName(book.title())
                    .quantity(orderItemDto.getQuantity())
                    .categoryIdList(orderItemDto.getCategoryIdList())
                    .packableProduct(book.packable())
                    .productSinglePrice(book.productPriceSales())
                    .build()
            );

        }

        clientOrderForm.setProductTotalAmount(productTotalAmount);
        clientOrderForm.setOrderTotalAmount(productTotalAmount + shippingPolicy.shippingFee());
        clientOrderForm.setTotalQuantity(totalQuantity);

        // 결제 수단 정보
        List<PaymentMethodResponseDto> paymentMethodList = paymentService.getPaymentMethodList(headers);

        // 가용 포인트
        Long usablePoint = orderPointClient.findPoint(headers).getTotalPoint();

        // 회원 적립률
        Long pointAccumulationRate = orderPointClient.findPoint(headers).getPointAccumulationRate();

        model.addAttribute("view", "clientOrder");

        model.addAttribute("clientOrderForm", clientOrderForm);
        model.addAttribute(PACKAGE_LIST, packagingService.getPackagingList(0).getBody());
        model.addAttribute(SHIPPING_POLICY, shippingPolicy);
        model.addAttribute("itemList", clientOrderForm.getOrderDetailDtoItemList());
        model.addAttribute("usablePoint", usablePoint);
        model.addAttribute("pointAccumulationRate", pointAccumulationRate);
        model.addAttribute("paymentMethodList", paymentMethodList);

        return INDEX;
    }

    public List<FindNonClientOrderIdInfoResponseDto> nonClientFindOrderId(HttpServletRequest request,
        FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto) {
        HttpHeaders headers = getHeader(request);
        return orderClient.findNonClientOrderId(findNonClientOrderIdRequestDto, headers).getBody();
    }

    public String nonClientFindOrderPassword(HttpServletRequest request, long orderId,
        UpdateNonClientOrderPasswordRequestDto updateNonClientOrderPasswordRequestDto) {
        HttpHeaders headers = getHeader(request);
        return orderClient.updateNonClientOrderPassword(headers, orderId,
            updateNonClientOrderPasswordRequestDto).getBody();
    }

    public List<OrderCouponDiscountInfo> getOrderCouponDiscountInfoList(HttpServletRequest request, CouponDiscountInfoRequestDto requestDto){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(request, "access"));
        return orderClient.getCouponDiscountInfoList(headers, requestDto).getBody();
    }
}