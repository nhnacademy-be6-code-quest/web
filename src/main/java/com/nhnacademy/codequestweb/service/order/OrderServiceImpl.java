package com.nhnacademy.codequestweb.service.order;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.client.coupon.CouponClient;
import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.order.OrderReviewClient;
import com.nhnacademy.codequestweb.client.point.OrderPointClient;
import com.nhnacademy.codequestweb.request.order.client.ClientOrderUsageCouponInfoDto;
import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.coupon.CouponOrderResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
//import com.nhnacademy.codequestweb.response.order.client.ClientCouponUsableDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderGetResponseDto;
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

import java.util.*;


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

        // 쿠폰아이디 - 사용 가능 여부
        Map<Long, Boolean> couponUsability = new HashMap(); // 상품 또는 카테고리에 적용 가능한지 + 일반 할인 쿠폰
        // 쿠폰아이디 - 적용 가능 상품 아이디 리스트
        Map<Long, Set<Long>> appliableCouponProductListMap = new HashMap(); // 쿠폰을 적용할 수 있는 상품 리스트

//        for(CouponOrderResponseDto couponOrderResponseDto : couponList){
//            // 일반 할인 쿠폰인지 체크
//            if(couponOrderResponseDto.getProductCoupon() == null &&
//                    couponOrderResponseDto.getCategoryCoupon() == null &&
//                    couponOrderResponseDto.getCouponPolicy() != null){
//                couponUsability.put(couponOrderResponseDto.getCouponId(), true);
//            }
//            // 카테고리 적용 쿠폰
//            else if(couponOrderResponseDto.getCategoryCoupon() != null){
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
//                Long applicableProductCategoryId = couponOrderResponseDto.getCouponId();
//                for(OrderItemDto orderItemDto : orderItemDtoList){ // orderItem에 쿠폰을 적용할 수 있는지 없는지 확인.
//                    if(){
//
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
}