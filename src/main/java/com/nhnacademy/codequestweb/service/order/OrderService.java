package com.nhnacademy.codequestweb.service.order;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.client.coupon.CouponClient;
import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.order.OrderReviewClient;
import com.nhnacademy.codequestweb.client.point.OrderPointClient;
import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.coupon.CouponOrderResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.order.client.*;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderGetResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import com.nhnacademy.codequestweb.service.mypage.MyPageService;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.service.product.PackagingService;
import com.nhnacademy.codequestweb.service.shipping.ShippingPolicyService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private static final String ID_HEADER = "X-User-Id";
    private static final String INDEX = "index";
    private static final String CLIENT_ORDER_FORM = "clientOrderForm";
    private static final String CLIENT_ORDER_DISCOUNT_FORM = "clientOrderDiscountForm";
    private static final String CLIENT_ORDER_PAYMENT_METHOD_FORM = "clientOrderPayMethodForm";
    private static final String AMOUNTDISCOUNT = "AMOUNTDISCOUNT";
    private static final String PERCENTAGEDISCOUNT = "PERCENTAGEDISCOUNT";
    private static final String PACKAGE_LIST = "packageList";
    private static final String SHIPPING_POLICY = "shippingPolicy";


    private final OrderClient orderClient;
    private final OrderReviewClient orderReviewClient;
    private final CouponClient couponClient;
    private final MyPageService myPageService;
    private final BookProductService bookProductService;
    private final ShippingPolicyService shippingPolicyService;
    private final OrderPointClient orderPointClient;
    private final PackagingService packagingService;

    public String viewClientOrder(HttpServletRequest req, Model model, List<String> orderItemDtoStringList) {
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

    public String viewClientOrderDiscount(HttpServletRequest req, Model model) {

        log.info("쿠폰 당 할인 정보 계산, 포인트 및 쿠폰 할인정보 뷰");

        HttpHeaders headers = getHeader(req);

        ClientOrderForm clientOrderForm = (ClientOrderForm) req.getSession().getAttribute(CLIENT_ORDER_FORM);

        ClientOrderDiscountForm clientOrderDiscountForm = ClientOrderDiscountForm.builder().payAmount(clientOrderForm.getProductTotalAmount() + clientOrderForm.getShippingFee()).build();

        // 가용 포인트
        Long usablePoint = orderPointClient.findPoint(headers).getTotalPoint();

        // 쿠폰 리스트
        List<CouponOrderResponseDto> couponList = couponClient.findClientCoupon(headers);

        // 쿠폰 할인 후 상품 정보 리스트
        List<OrderCouponDiscountInfo> couponDiscountInfoList = new ArrayList<>();

        for(CouponOrderResponseDto coupon : couponList) {
            OrderCouponDiscountInfo orderCouponDiscountInfo = orderCouponDiscountInfo(coupon, clientOrderForm);
            couponDiscountInfoList.add(orderCouponDiscountInfo);
        }

        model.addAttribute("view", "clientOrderDiscount");

        model.addAttribute(CLIENT_ORDER_FORM, clientOrderForm);
        model.addAttribute(CLIENT_ORDER_DISCOUNT_FORM, clientOrderDiscountForm); // 바인딩 객체
        model.addAttribute("usablePoint", usablePoint);
        model.addAttribute("couponList", couponList);
        model.addAttribute("couponDiscountInfoList", couponDiscountInfoList);

        req.setAttribute("couponDiscountInfoList", couponDiscountInfoList);

        return INDEX;
    }

    public String  saveClientTemporalOrder(HttpServletRequest req){

        HttpHeaders headers = getHeader(req);

        log.info("주문 서비스 레디스에 저장할 회원 주문 데이터 dto 생성");

        ClientOrderForm clientOrderForm = (ClientOrderForm) req.getSession().getAttribute(CLIENT_ORDER_FORM);
        ClientOrderDiscountForm clientOrderDiscountForm = (ClientOrderDiscountForm) req.getSession().getAttribute(CLIENT_ORDER_DISCOUNT_FORM);
        ClientOrderPayMethodForm clientOrderPayMethodForm = (ClientOrderPayMethodForm) req.getSession().getAttribute(CLIENT_ORDER_PAYMENT_METHOD_FORM);

        // 포인트 할인 적용하기
        ClientOrderCreateForm clientOrderCreateForm = ClientOrderCreateForm.builder()
                .couponId(clientOrderDiscountForm.getCouponId())
                .shippingFee(clientOrderForm.getShippingFee())
                .productTotalAmount(clientOrderForm.getProductTotalAmount())
                .payAmount(clientOrderDiscountForm.getPayAmount() == null ? 0 : clientOrderDiscountForm.getPayAmount())
                .couponDiscountAmount(clientOrderDiscountForm.getCouponDiscountAmount() == null ? 0 : clientOrderDiscountForm.getCouponDiscountAmount())
                .usedPointDiscountAmount(clientOrderDiscountForm.getUsedPointDiscountAmount() == null ? 0 : clientOrderDiscountForm.getUsedPointDiscountAmount())
                .orderedPersonName(clientOrderForm.getOrderedPersonName())
                .phoneNumber(clientOrderForm.getPhoneNumber())
                .addressNickname(clientOrderForm.getAddressNickname())
                .addressZipCode(clientOrderForm.getAddressZipCode())
                .deliveryAddress(clientOrderForm.getDeliveryAddress())
                .useDesignatedDeliveryDate(clientOrderForm.getUseDesignatedDeliveryDate() != null && clientOrderForm.getUseDesignatedDeliveryDate())
                .designatedDeliveryDate(clientOrderForm.getDesignatedDeliveryDate())
                .paymentMethod(clientOrderPayMethodForm.getPaymentMethod())
                .accumulatePoint(clientOrderPayMethodForm.getExpectedAccumulatingPoint())
                .tossOrderId(UUID.randomUUID().toString())
                .build();

        for(ClientOrderForm.OrderDetailDtoItem item : clientOrderForm.getOrderDetailDtoItemList()){
            clientOrderCreateForm.addOrderDetailDtoItem(
                    ClientOrderCreateForm.OrderDetailDtoItem.builder()
                            .productId(item.getProductId())
                            .productName(item.getProductName())
                            .quantity(item.getQuantity())
                            .categoryIdList(item.getCategoryIdList())
                            .productSinglePrice(item.getProductSinglePrice())
                            .packableProduct(item.getPackableProduct())
                            .usePackaging(item.getUsePackaging())
                            .optionProductId(item.getOptionProductId())
                            .optionProductName(item.getOptionProductName())
                            .optionProductSinglePrice(item.getOptionProductSinglePrice())
                            .optionQuantity(item.getOptionQuantity())
                            .build()
            );
        }

        orderClient.saveClientTemporalOrder(headers, clientOrderCreateForm);

        return clientOrderCreateForm.getTossOrderId();
    }

    public ClientOrderCreateForm getClientTemporalOrder(HttpHeaders headers, String tossOrderId){
        return orderClient.getClientTemporalOrder(headers, tossOrderId).getBody();
    }

    public String saveNonClientTemporalOrder(HttpServletRequest request, NonClientOrderForm nonClientOrderForm){
        nonClientOrderForm.setTossOrderId(UUID.randomUUID().toString());
        return orderClient.saveNonClientTemporalOrder(getHeader(request), nonClientOrderForm).getBody();
    }

    public String viewClientOrderPayMethod(HttpServletRequest req, Model model) {

        HttpHeaders headers = getHeader(req);

        log.info("적립 포인트 계산 및 결제수단 뷰");

        ClientOrderForm clientOrderForm = (ClientOrderForm) req.getSession().getAttribute(CLIENT_ORDER_FORM);
        ClientOrderDiscountForm clientOrderDiscountForm = (ClientOrderDiscountForm) req.getSession().getAttribute(CLIENT_ORDER_DISCOUNT_FORM);

        // 및 포인트 적립률
        Long pointAccumulationRate = orderPointClient.findPoint(headers).getPointAccumulationRate();

        // ** 바인딩 객체 **
        Long expectedAccumulatingPoint = Math.round(clientOrderDiscountForm.getPayAmount() * pointAccumulationRate * 0.01);
        ClientOrderPayMethodForm clientOrderPayMethodForm = ClientOrderPayMethodForm.builder()
                        .expectedAccumulatingPoint(expectedAccumulatingPoint).build();

        model.addAttribute("view", "clientOrderPayMethod");

        model.addAttribute(CLIENT_ORDER_FORM, clientOrderForm);
        model.addAttribute(CLIENT_ORDER_DISCOUNT_FORM, clientOrderDiscountForm);
        model.addAttribute("pointAccumulationRate", pointAccumulationRate);
        model.addAttribute(CLIENT_ORDER_PAYMENT_METHOD_FORM, clientOrderPayMethodForm);

        return INDEX;
    }

    public String viewNonClientOrder(HttpServletRequest req, Model model, OrderItemDto orderItemDto) {

        log.info("비회원 단건 주문 페이지 로딩");

        HttpHeaders headers = getHeader(req);

        // ** 바인딩할 객체 **
        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        // ** 화면에 뿌릴 정보 **
        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(headers, "비회원배송비");

        // 책 상세 정보
        BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getProductId()).getBody();
        // 폼에 추가
        assert book != null;
        nonClientOrderForm.addOrderDetailDtoItem(
                NonClientOrderForm.OrderDetailDtoItem.builder()
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

    public String viewNonClientOrder(HttpServletRequest req, Model model, List<String> orderItemDtoStringList) {

        log.info("비회원 주문 시도!");

        HttpHeaders headers = getHeader(req);

        List<OrderItemDto> orderItemDtoList = convertToOrderItemDtoList(orderItemDtoStringList);

        // ** 바인딩할 객체 **
        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        // ** 화면에 뿌릴 정보 **
        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(headers, "비회원배송비");

        // 바인딩 객체에 주문 상품 가격 정보 추가.
        orderItemDtoList.forEach(orderItemDto->{
            // 책 상세 정보
            BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getProductId()).getBody();
            assert book != null;
            // 폼에 추가
            nonClientOrderForm.addOrderDetailDtoItem(
                    NonClientOrderForm.OrderDetailDtoItem.builder()
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

    public Long createClientOrder(HttpServletRequest req, ClientOrderCreateForm clientOrderCreateForm){
        return orderClient.createClientOrder(getHeader(req), clientOrderCreateForm).getBody();
    }

    public Long createNonClientOrder(HttpServletRequest req, NonClientOrderForm nonClientOrderForm) {
        return orderClient.createNonClientOrder(getHeader(req), nonClientOrderForm).getBody();
    }

    public ResponseEntity<String> getOrderStatus(Long orderDetailId){
        return orderReviewClient.getOrderStatus(orderDetailId);
    }

    private List<OrderItemDto> convertToOrderItemDtoList(List<String> orderItemDtoStringList) {
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        try{
            String listString = orderItemDtoStringList.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            List<CartGetResponseDto> json2 = objectMapper.readValue(listString, new TypeReference<List<CartGetResponseDto>>() {});
            orderItemDtoList = json2.stream()
                    .map(cartGetResponseDto -> OrderItemDto.builder()
                            .productId(cartGetResponseDto.productId())
                            .quantity(cartGetResponseDto.productQuantityOfCart())
                            .categoryIdList(cartGetResponseDto.categorySet().stream().map(ProductCategory::productCategoryId
                            ).toList())
                            .packable(cartGetResponseDto.packable())
                            .build())
                    .toList();
        } catch (JsonProcessingException e) {
            log.debug("카테고리 목록을 orderItemDto리스트로 변환하는데 오류가 발생했습니다.");
        }
        return orderItemDtoList;
    }

    public Page<ClientOrderGetResponseDto> getClientOrders(HttpHeaders headers, int pageSize, int pageNo, String sortBy, String sortDir) {
        return orderClient.getClientOrders(headers, pageSize, pageNo, sortBy, sortDir).getBody();
    }

    public ClientOrderGetResponseDto getClientOrder(HttpHeaders headers, long orderId) {
        return orderClient.getClientOrder(headers, orderId).getBody();
    }

    public void paymentCompleteClientOrder(HttpHeaders headers, long orderId) {
        orderClient.paymentCompleteClientOrder(headers, orderId);
    }

    public void cancelClientOrder(HttpHeaders headers, long orderId) {
        orderClient.cancelClientOrder(headers, orderId);
    }

    public void refundClientOrder(HttpHeaders headers, long orderId) {
        orderClient.refundClientOrder(headers, orderId);
    }

    public void paymentCompleteNonClientOrder(HttpHeaders headers, long orderId) {
        orderClient.paymentCompleteNonClientOrder(headers, orderId);
    }

    public NonClientOrderGetResponseDto findNonClientOrder(HttpHeaders headers, String tossOrderId, String orderPassword) {
        return orderClient.findNonClientOrder(headers, tossOrderId, orderPassword).getBody();
    }

    public void updateOrderStatus(HttpHeaders headers, long orderId, String status) {
        orderClient.updateOrderStatus(headers, orderId, status);
    }

    private HttpHeaders getHeader(HttpServletRequest req){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        return headers;
    }

    private OrderCouponDiscountInfo orderCouponDiscountInfo(CouponOrderResponseDto coupon, ClientOrderForm clientOrderForm) {

        log.info("쿠폰 할인 정보 계산");

        List<ClientOrderForm.OrderDetailDtoItem> orderDetailDtoItemList = clientOrderForm.getOrderDetailDtoItemList();

        if (isAmountDiscount(coupon)) {
            return getOrderCouponDiscountInfoUsingAmountDiscount(
                coupon, clientOrderForm.getProductTotalAmount(),
                clientOrderForm.getTotalQuantity(), orderDetailDtoItemList,
                "전체 상품 주문 금액이 최소 주문 금액에 못 미칩니다"
            );
        }

        if (isPercentageDiscount(coupon)) {
            return getOrderCouponDiscountInfoUsingPercentageDiscount(
                coupon, clientOrderForm.getProductTotalAmount(),
                clientOrderForm.getTotalQuantity(), orderDetailDtoItemList,
                "전체 상품 주문 금액이 최소 주문 금액에 못 미칩니다"
            );
        }

        if (coupon.getProductCoupon() != null) {
            return processProductCoupon(coupon, orderDetailDtoItemList);
        }

        if (coupon.getCategoryCoupon() != null) {
            return processCategoryCoupon(coupon, orderDetailDtoItemList);
        }

        return createNotApplicableOrderCouponDiscountInfo(coupon, "쿠폰을 적용할 수 없습니다");
    }

    private boolean isAmountDiscount(CouponOrderResponseDto coupon) {
        return coupon.getProductCoupon() == null && coupon.getCategoryCoupon() == null &&
            coupon.getCouponPolicyDto() != null &&
            coupon.getCouponPolicyDto().getDiscountType().equals(AMOUNTDISCOUNT);
    }

    private boolean isPercentageDiscount(CouponOrderResponseDto coupon) {
        return coupon.getProductCoupon() == null && coupon.getCategoryCoupon() == null &&
            coupon.getCouponPolicyDto() != null &&
            coupon.getCouponPolicyDto().getDiscountType().equals(PERCENTAGEDISCOUNT);
    }

    private OrderCouponDiscountInfo processProductCoupon(CouponOrderResponseDto coupon, List<ClientOrderForm.OrderDetailDtoItem> orderDetailDtoItemList) {
        Long applicableProductId = coupon.getProductCoupon().getProductId();

        for (ClientOrderForm.OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList) {
            if (orderDetailDtoItem.getProductId().equals(applicableProductId)) {
                if (coupon.getCouponPolicyDto().getDiscountType().equals(AMOUNTDISCOUNT)) {
                    return getOrderCouponDiscountInfoUsingAmountDiscount(coupon,
                        orderDetailDtoItem.getProductSinglePrice() * orderDetailDtoItem.getQuantity(),
                        orderDetailDtoItem.getQuantity(), List.of(orderDetailDtoItem),
                        "쿠폰 적용가능한 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
                }
                if (coupon.getCouponPolicyDto().getDiscountType().equals(PERCENTAGEDISCOUNT)) {
                    return getOrderCouponDiscountInfoUsingPercentageDiscount(coupon,
                        orderDetailDtoItem.getProductSinglePrice() * orderDetailDtoItem.getQuantity(),
                        orderDetailDtoItem.getQuantity(), List.of(orderDetailDtoItem),
                        "쿠폰 적용가능한 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
                }
            }
        }

        return createNotApplicableOrderCouponDiscountInfo(coupon, "쿠폰을 적용할 수 있는 상품을 주문하지 않았습니다");
    }

    private OrderCouponDiscountInfo processCategoryCoupon(CouponOrderResponseDto coupon, List<ClientOrderForm.OrderDetailDtoItem> orderDetailDtoItemList) {
        Long applicableCategoryId = coupon.getCategoryCoupon().getProductCategoryId();
        List<ClientOrderForm.OrderDetailDtoItem> applicableProduct = new ArrayList<>();
        long totalQuantity = 0;
        long totalPrice = 0;

        for (ClientOrderForm.OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList) {
            if (orderDetailDtoItem.getCategoryIdList() == null) break;
            if (orderDetailDtoItem.getCategoryIdList().contains(applicableCategoryId)) {
                applicableProduct.add(orderDetailDtoItem);
                totalQuantity += orderDetailDtoItem.getQuantity();
                totalPrice += orderDetailDtoItem.getProductSinglePrice() * orderDetailDtoItem.getQuantity();
            }
        }

        if (!applicableProduct.isEmpty()) {
            if (coupon.getCouponPolicyDto().getDiscountType().equals(AMOUNTDISCOUNT)) {
                return getOrderCouponDiscountInfoUsingAmountDiscount(coupon, totalPrice, totalQuantity, applicableProduct, "쿠폰 적용가능한 카테고리 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
            }
            if (coupon.getCouponPolicyDto().getDiscountType().equals(PERCENTAGEDISCOUNT)) {
                return getOrderCouponDiscountInfoUsingPercentageDiscount(coupon, totalPrice, totalQuantity, applicableProduct, "쿠폰 적용가능한 카테고리 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
            }
        }

        return createNotApplicableOrderCouponDiscountInfo(coupon, "쿠폰을 적용할 수 있는 카테고리 상품을 구매하지 않았습니다");
    }

    private OrderCouponDiscountInfo createNotApplicableOrderCouponDiscountInfo(CouponOrderResponseDto coupon, String description) {
        OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
            .couponId(coupon.getCouponId())
            .isApplicable(false)
            .build();
        orderCouponDiscountInfo.updateNotApplicableDescription(description);
        return orderCouponDiscountInfo;
    }



    // coupon: 적용할 쿠폰
    // productTotalAmount: 쿠폰의 '최소 주문 금액'을 체크하기 위한 총 액수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 액수가 됨.
    // totalQuantity: 쿠폰을 적용시켜 할인 받을 상품의 총 개수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 수량이 됨.
    // orderDetailDtoItemList: 쿠폰 적용 대상이 되는 상품들.
    private OrderCouponDiscountInfo getOrderCouponDiscountInfoUsingAmountDiscount(CouponOrderResponseDto coupon, Long productTotalAmount, Long totalQuantity, List<ClientOrderForm.OrderDetailDtoItem> orderDetailDtoItemList, String updateNotApplicableDescription){

        // 최소 금액 기준이 맞지 않음 => 쿠폰 사용 불가.
        if(productTotalAmount < coupon.getCouponPolicyDto().getMinPurchaseAmount()){
            OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
                    .couponId(coupon.getCouponId())
                    .isApplicable(false)
                    .build();
            orderCouponDiscountInfo.updateNotApplicableDescription(updateNotApplicableDescription);
            return orderCouponDiscountInfo;
        }

        // 쿠폰 할인 금액
        long discountValue = coupon.getCouponPolicyDto().getDiscountValue();

        // 각 상품 당 할인 금액
        long discountValuePerProduct = Math.round((double) discountValue / totalQuantity);

        OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
                .couponId(coupon.getCouponId())
                .isApplicable(true)
                .discountTotalAmount(discountValue)
                .build();

        for(ClientOrderForm.OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList){
            orderCouponDiscountInfo.addProductPriceInfo(orderDetailDtoItem.getProductId(), orderDetailDtoItem.getProductSinglePrice() - discountValuePerProduct);
        }

        return orderCouponDiscountInfo;

    }

    // coupon: 적용할 쿠폰
    // productTotalAmount: 쿠폰의 '최소 주문 금액'을 체크하기 위한 총 액수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 액수가 됨.
    // totalQuantity: 쿠폰을 적용시켜 할인 받을 상품의 총 개수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 수량이 됨.
    // orderDetailDtoItemList: 쿠폰 적용 대상이 되는 상품들.
    private OrderCouponDiscountInfo getOrderCouponDiscountInfoUsingPercentageDiscount(CouponOrderResponseDto coupon, Long productTotalAmount, Long totalQuantity,
                                                                                      List<ClientOrderForm.OrderDetailDtoItem> orderDetailDtoItemList, String updateNotApplicableDescription){

        // 최소 금액 기준이 맞지 않음 => 쿠폰 사용 불가.
        if(productTotalAmount < coupon.getCouponPolicyDto().getMinPurchaseAmount()){
            OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
                    .couponId(coupon.getCouponId())
                    .isApplicable(false)
                    .build();
            orderCouponDiscountInfo.updateNotApplicableDescription(updateNotApplicableDescription);
            return orderCouponDiscountInfo;
        }

        // 할인금액 계산
        long discountValue = Math.round(productTotalAmount * (0.01 * coupon.getCouponPolicyDto().getDiscountValue()));
        // 할인 금액이 쿠폰의 '최대 할인 금액'을 초과할 경우.
        if(coupon.getCouponPolicyDto().getMaxDiscountAmount() < discountValue){
            discountValue = coupon.getCouponPolicyDto().getMaxDiscountAmount();
        }
        // 상품 별 할인 금액
        long discountValuePerProduct = Math.round((double) discountValue / totalQuantity);
        discountValue = discountValuePerProduct * totalQuantity;

        OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
                .couponId(coupon.getCouponId())
                .isApplicable(true)
                .discountTotalAmount(discountValue)
                .build();

        for(ClientOrderForm.OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList){
            orderCouponDiscountInfo.addProductPriceInfo(orderDetailDtoItem.getProductId(), orderDetailDtoItem.getProductSinglePrice() - discountValuePerProduct);
        }

        return orderCouponDiscountInfo;

    }

    private String processCreatingClientOrder(HttpServletRequest request, Model model, List<OrderItemDto> orderItemDtoList){

        log.info("회원 주문 정보 입력 화면 진입");

        HttpHeaders headers = getHeader(request);

        ClientPrivacyResponseDto orderedPerson = myPageService.getPrivacy(headers).getBody();

        // 주소목록
        List<ClientDeliveryAddressResponseDto> deliveryAddressList = myPageService.getDeliveryAddresses(headers).getBody();

        // 핸드폰 번호
        List<ClientPhoneNumberResponseDto> phoneNumberList = myPageService.getPhoneNumbers(headers).getBody();

        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(headers, "회원배송비");

        // ** 바인딩할 객체 **
        assert orderedPerson != null;
        ClientOrderForm clientOrderForm = ClientOrderForm.builder()
                .orderedPersonName(orderedPerson.getClientName())
                .build();

        long totalQuantity = 0;
        for (OrderItemDto orderItemDto : orderItemDtoList) {

            totalQuantity += orderItemDto.getQuantity();

            // 책 상세 정보
            BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getProductId()).getBody();

            // 폼에 추가
            assert book != null;
            clientOrderForm.addOrderDetailDtoItem(
                    ClientOrderForm.OrderDetailDtoItem.builder()
                            .productId(orderItemDto.getProductId())
                            .productName(book.title())
                            .quantity(orderItemDto.getQuantity())
                            .categoryIdList(orderItemDto.getCategoryIdList())
                            .packableProduct(book.packable())
                            .productSinglePrice(book.productPriceSales())
                            .build()
            );
        }

        clientOrderForm.updateTotalQuantity(totalQuantity);

        model.addAttribute("view", "clientOrder");

        model.addAttribute(CLIENT_ORDER_FORM, clientOrderForm);
        model.addAttribute(PACKAGE_LIST, packagingService.getPackagingList(0).getBody());
        model.addAttribute(SHIPPING_POLICY, shippingPolicy);
        model.addAttribute("deliveryAddressList", deliveryAddressList);
        model.addAttribute("phoneNumberList", phoneNumberList);
        model.addAttribute("orderedPerson", orderedPerson);

        return INDEX;
    }
}