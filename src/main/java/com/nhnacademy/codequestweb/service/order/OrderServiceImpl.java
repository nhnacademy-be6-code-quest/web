package com.nhnacademy.codequestweb.service.order;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import com.nhnacademy.codequestweb.response.order.pack.PackageInfoResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import com.nhnacademy.codequestweb.service.mypage.MyPageService;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.service.shippingpolicy.ShippingPolicyService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final String ID_HEADER = "X-User-Id";

    private final OrderClient orderClient;
    private final OrderReviewClient orderReviewClient;
    private final CouponClient couponClient;
    private final MyPageService myPageService;
    private final BookProductService bookProductService;
    private final ShippingPolicyService shippingPolicyService;
    private final OrderPointClient orderPointClient;

    // 회원 장바구니 주문
    @Override
    public String viewClientOrder(HttpServletRequest req, Model model, @ModelAttribute List<String> orderItemDtoStringList){

        HttpHeaders headers = getHeader(req);

        List<OrderItemDto> orderItemDtoList = convertToOrderItemDtoList(orderItemDtoStringList);

        // 사용자 기본 정보
        ClientPrivacyResponseDto orderedPerson = myPageService.getPrivacy(headers).getBody();

        // ** 바인딩할 객체 **
        ClientOrderForm clientOrderForm = ClientOrderForm.builder().orderedPersonName(orderedPerson.getClientName()).build();

        // ** 화면에 뿌려 줄 정보들 **
        // 주소목록
        List<ClientDeliveryAddressResponseDto> deliveryAddressList = myPageService.getDeliveryAddresses(headers).getBody();

        // 핸드폰 번호
        List<ClientPhoneNumberResponseDto> phoneNumberList = myPageService.getPhoneNumbers(headers).getBody();

        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(headers, "도서산간지역외");

        // 쿠폰 정보
        List<CouponOrderResponseDto> couponList = couponClient.findClientCoupon(headers);


        // 바인딩 객체에 주문 상품 가격 정보 추가.
        orderItemDtoList.forEach((orderItemDto)->{
            // 책 상세 정보
            BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getProductId()).getBody();
            // 폼에 추가
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

        });

//        // 쿠폰아이디 - 사용 가능 여부
//        Set<Long> couponUsability = new HashSet<>(); // 상품 또는 카테고리에 적용 가능한지 + 일반 할인 쿠폰
//        // 쿠폰아이디 - 적용 가능 상품 아이디 리스트. 사용할 수 있는 카테고리 할인 쿠폰, 상품 할인 쿠폰이 존재할 때 참고할 데이터.
//        Map<Long, Set<Long>> appliableCouponProductListMap = new HashMap(); // 쿠폰을 적용할 수 있는 상품 리스트
//
//        for(CouponOrderResponseDto couponOrderResponseDto : couponList){
//            // 일반 할인 쿠폰인지 체크
//            if(couponOrderResponseDto.getProductCoupon() == null &&
//                    couponOrderResponseDto.getCategoryCoupon() == null &&
//                    couponOrderResponseDto.getCouponPolicyDto() != null){
//                couponUsability.add(couponOrderResponseDto.getCouponId());
//            }
//            // 카테고리 적용 쿠폰
//            else if(couponOrderResponseDto.getCategoryCoupon() != null){
//                // 적용할 수 있는 상품 카테고리 아이디
//                Long applicableProductCategoryId = couponOrderResponseDto.getCategoryCoupon().getProductCategoryId();
//                for(OrderItemDto orderItemDto : orderItemDtoList){ // orderItem에 쿠폰을 적용할 수 있는지 없는지 확인.
//                    if(orderItemDto.getCategoryIdList().contains(applicableProductCategoryId)){ // orderItem에 쿠폰을 적용할 수 있음
//                        if(appliableCouponProductListMap.get(couponOrderResponseDto.getCouponId()) == null){
//                            appliableCouponProductListMap.put(couponOrderResponseDto.getCouponId(), new HashSet());
//                            appliableCouponProductListMap.get(couponOrderResponseDto.getCouponId()).add(orderItemDto.getProductId());
//                        }
//                        else {
//                            appliableCouponProductListMap.get(couponOrderResponseDto.getCouponId()).add(orderItemDto.getProductId());
//                        }
//                    }
//                }
//            }
//            // 상품 적용 쿠폰
//            else if(couponOrderResponseDto.getProductCoupon() != null){
//                Long applicableProductId = couponOrderResponseDto.getProductCoupon().getProductId();
//                for(OrderItemDto orderItemDto : orderItemDtoList){ // orderItem에 쿠폰을 적용할 수 있는지 없는지 확인.
//                    if(applicableProductId.equals(orderItemDto.getProductId())){
//                        if(appliableCouponProductListMap.get(couponOrderResponseDto.getCouponId()) == null){
//                            appliableCouponProductListMap.put(couponOrderResponseDto.getCouponId(), new HashSet());
//                            appliableCouponProductListMap.get(couponOrderResponseDto.getCouponId()).add(orderItemDto.getProductId());
//                        }
//                        appliableCouponProductListMap.get(couponOrderResponseDto.getCouponId()).add(orderItemDto.getProductId());
//                    }
//                }
//            }
//        }

        // 포장지 정보
        List<PackageInfoResponseDto> packageList = getAllPackages();

        // 사용가능 포인트 정보
        Integer usablePoint = orderPointClient.findPoint(headers).getTotalPoint();

        model.addAttribute("view", "clientOrder");

        model.addAttribute("clientOrderForm", clientOrderForm);
        model.addAttribute("packageList", packageList);
        model.addAttribute("usablePoint", usablePoint);
        model.addAttribute("shippingPolicy", shippingPolicy);
        model.addAttribute("deliveryAddressList", deliveryAddressList);
        model.addAttribute("phoneNumberList", phoneNumberList);
        model.addAttribute("orderedPerson", orderedPerson);
        model.addAttribute("couponList", couponList);
        //model.addAttribute("couponUsability", couponUsability); // 주문페이지에서 사용가능한 유효한 쿠폰들. 추후 주문페이지에서 옵션 상품 선택에 따라 달라질 수 있음(최소주문금액 때문에)

        return "index";
    }

    // 회원 단건 주문
    public String viewClientOrder(HttpServletRequest req, Model model, OrderItemDto orderItemDto){

        HttpHeaders headers = getHeader(req);

        ClientPrivacyResponseDto orderedPerson = myPageService.getPrivacy(headers).getBody();

        // ** 바인딩할 객체 **
        ClientOrderForm clientOrderForm = ClientOrderForm.builder().orderedPersonName(orderedPerson.getClientName()).build();

        // 주소목록
        List<ClientDeliveryAddressResponseDto> deliveryAddressList = myPageService.getDeliveryAddresses(headers).getBody();

        // 핸드폰 번호
        List<ClientPhoneNumberResponseDto> phoneNumberList = myPageService.getPhoneNumbers(headers).getBody();

        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(headers, "도서산간지역외");

        // 바인딩 객체에 주문 상품 가격 정보 추가.
        // 책 상세 정보
        BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getProductId()).getBody();

        // 폼에 추가
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

        // 포장지 정보
        List<PackageInfoResponseDto> packageList = getAllPackages();

        // 쿠폰 정보
        List<CouponOrderResponseDto> couponList = couponClient.findClientCoupon(headers);

        Integer usablePoint = orderPointClient.findPoint(headers).getTotalPoint();

        model.addAttribute("view", "clientOrder");

        model.addAttribute("clientOrderForm", clientOrderForm);
        model.addAttribute("packageList", packageList);
        model.addAttribute("usablePoint", usablePoint);
        model.addAttribute("shippingPolicy", shippingPolicy);
        model.addAttribute("deliveryAddressList", deliveryAddressList);
        model.addAttribute("phoneNumberList", phoneNumberList);
        model.addAttribute("orderedPerson", orderedPerson);
        model.addAttribute("couponList", couponList);

        return "index";
    }





    // ###


    @Override
    public String viewClientOrder2(HttpServletRequest req, Model model, List<String> orderItemDtoStringList) {

        HttpHeaders headers = getHeader(req);

        ClientPrivacyResponseDto orderedPerson = myPageService.getPrivacy(headers).getBody();

        // 주소목록
        List<ClientDeliveryAddressResponseDto> deliveryAddressList = myPageService.getDeliveryAddresses(headers).getBody();

        // 핸드폰 번호
        List<ClientPhoneNumberResponseDto> phoneNumberList = myPageService.getPhoneNumbers(headers).getBody();

        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(headers, "도서산간지역외");

        // ** 바인딩할 객체 **
        ClientOrderForm2 clientOrderForm2 = ClientOrderForm2.builder()
                .orderedPersonName(orderedPerson.getClientName())
                .build();

        // 바인딩 객체에 주문 상품 가격 정보 추가.
        List<OrderItemDto> orderItemDtoList = convertToOrderItemDtoList(orderItemDtoStringList);

        long totalQuantity = 0;
        for (OrderItemDto orderItemDto : orderItemDtoList) {

            totalQuantity += orderItemDto.getQuantity();

            // 책 상세 정보
            BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getProductId()).getBody();

            // 폼에 추가
            clientOrderForm2.addOrderDetailDtoItem(
                    ClientOrderForm2.OrderDetailDtoItem.builder()
                            .productId(orderItemDto.getProductId())
                            .productName(book.title())
                            .quantity(orderItemDto.getQuantity())
                            .categoryIdList(orderItemDto.getCategoryIdList())
                            .packableProduct(book.packable())
                            .productSinglePrice(book.productPriceSales())
                            .build()
            );
        }

        clientOrderForm2.updateTotalQuantity(totalQuantity);

        // 포장지 정보
        List<PackageInfoResponseDto> packageList = getAllPackages();

        model.addAttribute("view", "clientOrder2");

        model.addAttribute("clientOrderForm2", clientOrderForm2);
        model.addAttribute("packageList", packageList);
        model.addAttribute("shippingPolicy", shippingPolicy);
        model.addAttribute("deliveryAddressList", deliveryAddressList);
        model.addAttribute("phoneNumberList", phoneNumberList);
        model.addAttribute("orderedPerson", orderedPerson);

        return "index";
    }

    @Override
    public String viewClientOrder2(HttpServletRequest req, Model model, OrderItemDto orderItemDto) {

        HttpHeaders headers = getHeader(req);

        ClientPrivacyResponseDto orderedPerson = myPageService.getPrivacy(headers).getBody();

        // ** 바인딩할 객체 **
        ClientOrderForm2 clientOrderForm2 = ClientOrderForm2.builder()
                .orderedPersonName(orderedPerson.getClientName())
                .build();

        clientOrderForm2.updateTotalQuantity(orderItemDto.getQuantity());

        // 주소목록
        List<ClientDeliveryAddressResponseDto> deliveryAddressList = myPageService.getDeliveryAddresses(headers).getBody();

        // 핸드폰 번호
        List<ClientPhoneNumberResponseDto> phoneNumberList = myPageService.getPhoneNumbers(headers).getBody();

        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(headers, "도서산간지역외");

        // 바인딩 객체에 주문 상품 가격 정보 추가.
        // 책 상세 정보
        BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getProductId()).getBody();

        // 폼에 추가
        clientOrderForm2.addOrderDetailDtoItem(
                ClientOrderForm2.OrderDetailDtoItem.builder()
                        .productId(orderItemDto.getProductId())
                        .productName(book.title())
                        .quantity(orderItemDto.getQuantity())
                        .categoryIdList(orderItemDto.getCategoryIdList())
                        .packableProduct(book.packable())
                        .productSinglePrice(book.productPriceSales())
                        .build()
        );

        // 포장지 정보
        List<PackageInfoResponseDto> packageList = getAllPackages();

        model.addAttribute("view", "clientOrder2");

        model.addAttribute("clientOrderForm2", clientOrderForm2);
        model.addAttribute("packageList", packageList);
        model.addAttribute("shippingPolicy", shippingPolicy);
        model.addAttribute("deliveryAddressList", deliveryAddressList);
        model.addAttribute("phoneNumberList", phoneNumberList);
        model.addAttribute("orderedPerson", orderedPerson);

        return "index";
    }

    @Override
    public String viewClientOrderDiscount(HttpServletRequest req, Model model) {

        HttpHeaders headers = getHeader(req);

        ClientOrderForm2 clientOrderForm2 = (ClientOrderForm2) req.getSession().getAttribute("clientOrderForm2");

        ClientOrderDiscountForm clientOrderDiscountForm = ClientOrderDiscountForm.builder().payAmount(clientOrderForm2.getProductTotalAmount() + clientOrderForm2.getShippingFee()).build();

        // 가용 포인트
        Integer usablePoint = orderPointClient.findPoint(headers).getTotalPoint();

        // 쿠폰 리스트
        List<CouponOrderResponseDto> couponList = couponClient.findClientCoupon(headers);

        // 쿠폰 할인 후 상품 정보 리스트
        List<OrderCouponDiscountInfo> couponDiscountInfoList = new ArrayList<>();

        for(CouponOrderResponseDto coupon : couponList) {
            OrderCouponDiscountInfo orderCouponDiscountInfo = orderCouponDiscountInfo(coupon, clientOrderForm2);
            couponDiscountInfoList.add(orderCouponDiscountInfo);
        }

        model.addAttribute("view", "clientOrderDiscount");

        model.addAttribute("clientOrderForm2", clientOrderForm2);
        model.addAttribute("clientOrderDiscountForm", clientOrderDiscountForm); // 바인딩 객체
        model.addAttribute("usablePoint", usablePoint);
        model.addAttribute("couponList", couponList);
        model.addAttribute("couponDiscountInfoList", couponDiscountInfoList);

        return "index";
    }

    @Override
    public String viewClientOrderPayMethod(HttpServletRequest req, Model model) {
        model.addAttribute("view", "clientOrderPayMethod");

        ClientOrderForm2 clientOrderForm2 = (ClientOrderForm2) req.getSession().getAttribute("clientOrderForm2");
        ClientOrderDiscountForm clientOrderDiscountForm = (ClientOrderDiscountForm) req.getSession().getAttribute("clientOrderDiscountForm");


        // 적립금 정보
        orderPointClient.


        return "index";
    }

    @Override
    public Long createClientOrder2(HttpServletRequest req, ClientOrderForm2 clientOrderForm2, ClientOrderDiscountForm clientOrderDiscountForm, ClientOrderPayMethodForm clientOrderPayMethodForm) {
        return 0L;
    }

    // ###

    @Override
    public String viewNonClientOrder(HttpServletRequest req, Model model, OrderItemDto orderItemDto) {
        HttpHeaders headers = getHeader(req);

        // ** 바인딩할 객체 **
        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        // ** 화면에 뿌릴 정보 **
        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(headers, "도서산간지역외");

        // 책 상세 정보
        BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getProductId()).getBody();
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

        List<PackageInfoResponseDto> packageList = getAllPackages();

        model.addAttribute("view", "nonClientOrder");

        model.addAttribute("nonClientOrderForm", nonClientOrderForm);
        model.addAttribute("packageList", packageList);
        model.addAttribute("shippingPolicy", shippingPolicy);


        return "index";
    }

    @Override
    public String viewNonClientOrder(HttpServletRequest req, Model model, List<String> orderItemDtoStringList) {
        HttpHeaders headers = getHeader(req);

        List<OrderItemDto> orderItemDtoList = convertToOrderItemDtoList(orderItemDtoStringList);

        // ** 바인딩할 객체 **
        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        // ** 화면에 뿌릴 정보 **
        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy(headers, "도서산간지역외");

        // 바인딩 객체에 주문 상품 가격 정보 추가.
        orderItemDtoList.forEach((orderItemDto)->{
            // 책 상세 정보
            BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getProductId()).getBody();
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

        List<PackageInfoResponseDto> packageList = getAllPackages();

        model.addAttribute("view", "nonClientOrder");

        model.addAttribute("nonClientOrderForm", nonClientOrderForm);
        model.addAttribute("packageList", packageList);
        model.addAttribute("shippingPolicy", shippingPolicy);

        return "index";
    }

    @Override
    public Long createClientOrder(HttpServletRequest req, ClientOrderForm clientOrderForm){
        return orderClient.createClientOrder(getHeader(req), clientOrderForm).getBody();
    }

    @Override
    public Long createNonClientOrder(HttpServletRequest req, NonClientOrderForm nonClientOrderForm) {
        return orderClient.createNonClientOrder(getHeader(req), nonClientOrderForm).getBody();
    }

    public ResponseEntity<String> getOrderStatus(Long orderDetailId){
        return orderReviewClient.getOrderStatus(orderDetailId);
    }

    private List<PackageInfoResponseDto> getAllPackages() {
        // 데이터베이스에 있는 모든 포장지 가져오기 TODO 추후 productService 대체하기
        PackageInfoResponseDto pack1 = new PackageInfoResponseDto(6, "곰돌이 포장지", 500);
        PackageInfoResponseDto pack2 = new PackageInfoResponseDto(7, "쇼핑백 포장", 800);
        List<PackageInfoResponseDto> res = new ArrayList<>();
        res.add(pack1);
        res.add(pack2);
        return res;
    }

    private List<OrderItemDto> convertToOrderItemDtoList(List<String> orderItemDtoStringList) {
        List<OrderItemDto> orderItemDtoList;
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
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            log.debug("카테고리 목록을 orderItemDto리스트로 변환하는데 오류가 발생했습니다.");
            throw new RuntimeException(e);
        }
        return orderItemDtoList;
    }

    @Override
    public Page<ClientOrderGetResponseDto> getClientOrders(HttpHeaders headers, int pageSize, int pageNo, String sortBy, String sortDir) {
        return orderClient.getClientOrders(headers, pageSize, pageNo, sortBy, sortDir).getBody();
    }


    @Override
    public ClientOrderGetResponseDto getClientOrder(HttpHeaders headers, long orderId) {
        return orderClient.getClientOrder(headers, orderId).getBody();
    }

    @Override
    public void paymentCompleteClientOrder(HttpHeaders headers, long orderId) {
        orderClient.paymentCompleteClientOrder(headers, orderId);
    }

    @Override
    public void cancelClientOrder(HttpHeaders headers, long orderId) {
        orderClient.cancelClientOrder(headers, orderId);
    }

    @Override
    public void refundClientOrder(HttpHeaders headers, long orderId) {
        orderClient.refundClientOrder(headers, orderId);
    }

    @Override
    public void paymentCompleteNonClientOrder(HttpHeaders headers, long orderId) {
        orderClient.paymentCompleteNonClientOrder(headers, orderId);
    }

    @Override
    public void cancelNonClientOrder(HttpHeaders headers, long orderId) {
        orderClient.cancelNonClientOrder(headers, orderId);
    }

    @Override
    public void refundNonClientOrder(HttpHeaders headers, long orderId) {
        orderClient.refundNonClientOrder(headers, orderId);
    }

    @Override
    public NonClientOrderGetResponseDto findNonClientOrder(HttpHeaders headers, long orderId, String orderPassword) {
        return orderClient.findNonClientOrder(headers, orderId, orderPassword).getBody();
    }

    @Override
    public void updateOrderStatus(HttpHeaders headers, long orderId, String status) {
        orderClient.updateOrderStatus(headers, orderId, status);
    }

    private HttpHeaders getHeader(HttpServletRequest req){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        return headers;
    }

    @Override
    public void getAllOrderList(HttpHeaders headers, Pageable pageable) {

    }

    private OrderCouponDiscountInfo orderCouponDiscountInfo(CouponOrderResponseDto coupon, ClientOrderForm2 clientOrderForm2){

        List<ClientOrderForm2.OrderDetailDtoItem> orderDetailDtoItemList = clientOrderForm2.getOrderDetailDtoItemList();

        // 일반 금액 할인
        if(coupon.getProductCoupon() == null
                && coupon.getCategoryCoupon() == null
                && coupon.getCouponPolicyDto() != null
                && coupon.getCouponPolicyDto().getDiscountType().equals("AMOUNTDISCOUNT")){

            return getOrderCouponDiscountInfoUsingAmountDiscount(coupon, clientOrderForm2.getProductTotalAmount(), clientOrderForm2.getTotalQuantity(), orderDetailDtoItemList, "전체 상품 주문 금액이 최소 주문 금액에 못 미칩니다");

        }

        // 일반 백분율 할인
        else if(coupon.getProductCoupon() == null
                && coupon.getCategoryCoupon() == null
                && coupon.getCouponPolicyDto() != null
                && coupon.getCouponPolicyDto().getDiscountType().equals("PERCENTAGEDISCOUNT")){

            return getOrderCouponDiscountInfoUsingPercentageDiscount(coupon, clientOrderForm2.getProductTotalAmount(), clientOrderForm2.getTotalQuantity(), orderDetailDtoItemList, "전체 상품 주문 금액이 최소 주문 금액에 못 미칩니다");

        }

        // 상품 할인
        else if(coupon.getProductCoupon() != null){

            // 쿠폰을 적용할 수 있는 상품 아이디
            Long applicableProductId = coupon.getProductCoupon().getProductId();

            // 쿠폰 적용 가능한 상품이 있는지 확인.
            for(ClientOrderForm2.OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList){
                if(orderDetailDtoItem.getProductId() == applicableProductId){
                    // 금액 할인권
                    if(coupon.getCouponPolicyDto().getDiscountType().equals("AMOUNTDISCOUNT")){
                        return getOrderCouponDiscountInfoUsingAmountDiscount(coupon,
                                orderDetailDtoItem.getProductSinglePrice() * orderDetailDtoItem.getQuantity(),
                                orderDetailDtoItem.getQuantity(), new ArrayList<>(List.of(orderDetailDtoItem)), "쿠폰 적용가능한 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
                    }
                    // 백분율 할인권
                    if(coupon.getCouponPolicyDto().getDiscountType().equals("PERCENTAGEDISCOUNT")){
                        return getOrderCouponDiscountInfoUsingPercentageDiscount(coupon,
                                orderDetailDtoItem.getProductSinglePrice() * orderDetailDtoItem.getQuantity(),
                                orderDetailDtoItem.getQuantity(), new ArrayList<>(List.of(orderDetailDtoItem)), "쿠폰 적용가능한 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
                    }
                }
            }

            // 적용할 상품이 없을 때
            OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
                        .couponId(coupon.getCouponId())
                        .isApplicable(false)
                        .build();

            orderCouponDiscountInfo.updateNotApplicableDescription("쿠폰을 적용할 수 있는 상품을 주문하지 않았습니다");

            return orderCouponDiscountInfo;

        }

        // 카테고리 할인
        else if(coupon.getCategoryCoupon() != null){

            Long applicableCategoryId = coupon.getCategoryCoupon().getProductCategoryId();

            List<ClientOrderForm2.OrderDetailDtoItem> applicableProduct = new ArrayList<>();

            long totalQuantity = 0;
            long totalPrice = 0;

            for(ClientOrderForm2.OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList){
                if(orderDetailDtoItem.getCategoryIdList() == null) break;
                if(orderDetailDtoItem.getCategoryIdList().contains(applicableCategoryId)){
                    applicableProduct.add(orderDetailDtoItem);
                    totalQuantity += orderDetailDtoItem.getQuantity();
                    totalPrice += orderDetailDtoItem.getProductSinglePrice() * orderDetailDtoItem.getQuantity();
                }
            }

            if(applicableProduct.size() > 0 && coupon.getCouponPolicyDto().getDiscountType().equals("AMOUNTDISCOUNT")){
                return getOrderCouponDiscountInfoUsingAmountDiscount(coupon, totalPrice, totalQuantity, applicableProduct, "쿠폰 적용가능한 카테고리 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
            }
            else if(applicableProduct.size() > 0 && coupon.getCouponPolicyDto().getDiscountType().equals("PERCENTAGEDISCOUNT")){
                return getOrderCouponDiscountInfoUsingPercentageDiscount(coupon, totalPrice, totalQuantity, applicableProduct, "쿠폰 적용가능한 카테고리 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
            }

            // 적용할 상품이 없을 때
            OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
                    .couponId(coupon.getCouponId())
                    .isApplicable(false)
                    .build();
            orderCouponDiscountInfo.updateNotApplicableDescription("쿠폰을 적용할 수 있는 카테고리 상품을 구매하지 않았습니다");
            return orderCouponDiscountInfo;

        }

        OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
                .couponId(coupon.getCouponId())
                .isApplicable(false)
                .build();

        orderCouponDiscountInfo.updateNotApplicableDescription("쿠폰을 적용할 수 없습니다");

        return orderCouponDiscountInfo;
    }

    // coupon: 적용할 쿠폰
    // productTotalAmount: 쿠폰의 '최소 주문 금액'을 체크하기 위한 총 액수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 액수가 됨.
    // totalQuantity: 쿠폰을 적용시켜 할인 받을 상품의 총 개수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 수량이 됨.
    // orderDetailDtoItemList: 쿠폰 적용 대상이 되는 상품들.
    private OrderCouponDiscountInfo getOrderCouponDiscountInfoUsingAmountDiscount(CouponOrderResponseDto coupon, Long productTotalAmount, Long totalQuantity, List<ClientOrderForm2.OrderDetailDtoItem> orderDetailDtoItemList, String updateNotApplicableDescription){

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

        for(ClientOrderForm2.OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList){
            orderCouponDiscountInfo.addProductPriceInfo(orderDetailDtoItem.getProductId(), orderDetailDtoItem.getProductSinglePrice() - discountValuePerProduct);
        }

        return orderCouponDiscountInfo;

    }

    // coupon: 적용할 쿠폰
    // productTotalAmount: 쿠폰의 '최소 주문 금액'을 체크하기 위한 총 액수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 액수가 됨.
    // totalQuantity: 쿠폰을 적용시켜 할인 받을 상품의 총 개수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 수량이 됨.
    // orderDetailDtoItemList: 쿠폰 적용 대상이 되는 상품들.
    private OrderCouponDiscountInfo getOrderCouponDiscountInfoUsingPercentageDiscount(CouponOrderResponseDto coupon, Long productTotalAmount, Long totalQuantity, List<ClientOrderForm2.OrderDetailDtoItem> orderDetailDtoItemList, String updateNotApplicableDescription){

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

        for(ClientOrderForm2.OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList){
            orderCouponDiscountInfo.addProductPriceInfo(orderDetailDtoItem.getProductId(), orderDetailDtoItem.getProductSinglePrice() - discountValuePerProduct);
        }

        return orderCouponDiscountInfo;

    }
}