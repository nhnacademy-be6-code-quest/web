document.addEventListener("DOMContentLoaded", function() {
    const useCouponBtn = document.getElementById("use-coupon");
    const couponList = document.querySelector(".coupon-list");
    const applyCouponBtn = document.getElementById("apply-coupon");
    const selectPaymentMethodBtn = document.getElementById("select-payment-method");
    const paymentMethodModal = document.querySelector(".payment-method-modal");
    const confirmPaymentMethodBtn = document.getElementById("confirm-payment-method");

    useCouponBtn.addEventListener("click", function() {
        couponList.style.display = "block";
    });

    applyCouponBtn.addEventListener("click", function() {
        // 선택된 쿠폰 정보 처리
        const selectedCoupon = document.querySelector(".coupon-list input[type='radio']:checked");
        if (selectedCoupon) {
            // 선택된 쿠폰 정보를 서버로 전송하는 코드 작성
            console.log("Selected coupon ID:", selectedCoupon.value);
        }
        couponList.style.display = "none";
    });

    selectPaymentMethodBtn.addEventListener("click", function() {
        paymentMethodModal.style.display = "flex";
    });

    confirmPaymentMethodBtn.addEventListener("click", function() {
        const selectedPaymentMethod = document.querySelector(".payment-method-modal input[name='payment-method']:checked");
        if (selectedPaymentMethod) {
            // 선택된 결제 방식 정보를 서버로 전송하는 코드 작성
            console.log("Selected payment method:", selectedPaymentMethod.value);
            paymentMethodModal.style.display = "none";
        }
    });
});
