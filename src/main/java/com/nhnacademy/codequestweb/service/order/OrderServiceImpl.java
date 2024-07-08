package com.nhnacademy.codequestweb.service.order;


import com.nhnacademy.codequestweb.client.coupon.CouponClient;
import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.order.OrderReviewClient;
import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.coupon.CouponOrderResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import com.nhnacademy.codequestweb.response.order.pack.PackageInfoResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import com.nhnacademy.codequestweb.service.mypage.MyPageService;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.service.shippingpolicy.ShippingPolicyService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String ID_HEADER = "X-User-Id";

    private final OrderClient orderClient;
    private final OrderReviewClient orderReviewClient;
    private final CouponClient couponClient;
    private final MyPageService myPageService;
    private final BookProductService bookProductService;
    private final ShippingPolicyService shippingPolicyService;

    public String viewClientOrder(HttpServletRequest req, Model model){

        HttpHeaders headers = getHeader(req);

        List<OrderItemDto> orderItemDtoList = getOrderItemDtoList();

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
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy("도서산간지역외");

        // 바인딩 객체에 주문 상품 가격 정보 추가.
        orderItemDtoList.forEach((orderItemDto)->{
            // 책 상세 정보
            BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getBookId()).getBody();
            // 폼에 추가
            clientOrderForm.addOrderDetailDtoItem(
                    ClientOrderForm.OrderDetailDtoItem.builder()
                            .productId(orderItemDto.getProductId())
                            .productName(book.title())
                            .quantity(orderItemDto.getQuantity())
                            .categoryIdList(orderItemDto.getCategoryIdList())
                            .bookId(orderItemDto.getBookId())
                            .productSinglePrice(book.productPriceSales())
                            .build()
            );
        });

        // 포장지 정보
        List<PackageInfoResponseDto> packageList = getAllPackages();

        // 쿠폰 정보
        List<CouponOrderResponseDto> couponList = couponClient.findClientCoupon(headers);

        // 사용가능 포인트 정보
        long usablePoint = 10000l; // TODO 추후 포인트 서비스에서 가져오기

        model.addAttribute("clientOrderForm", clientOrderForm);
        model.addAttribute("packageList", packageList);
        model.addAttribute("usablePoint", usablePoint);
        model.addAttribute("shippingPolicy", shippingPolicy);
        model.addAttribute("deliveryAddressList", deliveryAddressList);
        model.addAttribute("phoneNumberList", phoneNumberList);
        model.addAttribute("orderedPerson", orderedPerson);
        model.addAttribute("couponList", couponList);

        return "view/order/clientOrder";
    }

    @Override
    public String viewNonClientOrder(HttpServletRequest req, Model model) {
        HttpHeaders headers = getHeader(req);

        List<OrderItemDto> orderItemDtoList = getOrderItemDtoList();

        // ** 바인딩할 객체 **
        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        // ** 화면에 뿌릴 정보 **
        // 배송비 정책
        ShippingPolicyGetResponseDto shippingPolicy = shippingPolicyService.getShippingPolicy("도서산간지역외");


        // 바인딩 객체에 주문 상품 가격 정보 추가.
        orderItemDtoList.forEach((orderItemDto)->{
            // 책 상세 정보
            BookProductGetResponseDto book = bookProductService.getSingleBookInfo(headers, orderItemDto.getBookId()).getBody();
            // 폼에 추가
            nonClientOrderForm.addOrderDetailDtoItem(
                    NonClientOrderForm.OrderDetailDtoItem.builder()
                            .productId(orderItemDto.getProductId())
                            .productName(book.title())
                            .quantity(orderItemDto.getQuantity())
                            .bookId(orderItemDto.getBookId())
                            .productSinglePrice(book.productPriceSales())
                            .build()
            );
        });

        List<PackageInfoResponseDto> packageList = getAllPackages();


        model.addAttribute("nonClientOrderForm", nonClientOrderForm);
        model.addAttribute("packageList", packageList);
        model.addAttribute("shippingPolicy", shippingPolicy);

        return "view/order/nonClientOrder";
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

    private List<OrderItemDto> getOrderItemDtoList(){
        return new ArrayList<>(Arrays.asList(
                new OrderItemDto(4L, 1L, new ArrayList<>(List.of(1L)), 2L),
                new OrderItemDto(5L,3L, new ArrayList<>(List.of(4L)), 3L)
        ));
    }

    private HttpHeaders getHeader(HttpServletRequest req){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        return headers;
    }
}