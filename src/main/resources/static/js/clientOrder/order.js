document.addEventListener('DOMContentLoaded', function () {
  console.log("주문 자바 스크립트 로딩");
  // 주소찾기 외부 api 임포트
  loadExternalScript("//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js", function() {
    console.log("External script loaded successfully.");
  });
  // 캘린더 선택 가능 날짜 초기화
  document.querySelector("input[type=date]").min = getMinDeliveryDate();
  // 가격 정보 업데이트
  updateProductTotalAmountClient();
  console.log("안");
  updateShippingFee();
  console.log("뇽");
  updatePayAmountClient();
  // 번호선택 버튼에 이벤트 리스너 달기
  document.querySelector('button[data-bs-target="#phoneNumberSelectModal"]').addEventListener(
      'click', loadPhoneNumberList)
  // 주소 선택 버튼에 이벤트 리스너 달기
  document.querySelector('button[data-bs-target="#addressSelectModal"]').addEventListener('click',
      loadAddressList)
});

// 자바스크립트 파일에서 외부 스크립트를 동적으로 로드하는 함수
function loadExternalScript(url, callback) {
  // 스크립트 요소를 생성
  const script = document.createElement('script');
  script.src = url;
  script.async = true; // 비동기 로딩

  // 스크립트 로드가 완료되면 콜백 호출
  script.onload = function() {
    if (callback) callback();
  };

  // 에러 발생 시 콜백 호출
  script.onerror = function() {
    console.error(`Failed to load script: ${url}`);
  };

  // 문서의 head에 스크립트 추가
  document.head.appendChild(script);
}

function updateExpectedAccumulatingPoint(){
  const rate = parseInt(document.getElementById("pointAccumulationRate").textContent);
  const payAmount = parseInt(document.getElementById("payAmount").value);
  document.getElementById("expectedAccumulatingPoint").textContent = (rate * 0.01 * payAmount).toString();
}

<!--포장여부 선택 관련-->
function handleOnChangeUsedPackaging(checked, index) {

  // 쿠폰 및 포인트 할인 적용 취소
  document.getElementById("couponId").value = null;
  document.getElementById("selectedCouponName").textContent = null;
  document.getElementById("selectedCouponDiscountAmount").textContent = 0;
  document.getElementById("couponDiscountAmount").value = 0;

  document.getElementById("usedPointDiscountAmount").value = 0;
  document.getElementById('inputUsedPointAmount').value = 0;

  document.getElementById("selectedCouponInfo").hidden = true;


  const packageSelectContainer = document.getElementById("packageSelectContainer[" + index + "]");
  packageSelectContainer.hidden = !checked;
  if (checked) {
    packageSelectContainer.querySelector('select').selectedIndex = 0;
  }
  document.getElementById("orderDetailDtoItemList" + index + ".optionProductId").value = null;
  document.getElementById("orderDetailDtoItemList" + index + ".optionProductName").value = null;
  document.getElementById("orderDetailDtoItemList" + index + ".optionProductSinglePrice").value = 0;
  updateProductTotalAmountClient();
  updateShippingFee();
  updatePayAmountClient();
  updateExpectedAccumulatingPoint();

  const productTotalAmount = parseInt(document.getElementById("productTotalAmount").value);
  const shippingFee = parseInt(document.getElementById("shippingFee").value)

  document.getElementById("orderTotalAmount").value = productTotalAmount + shippingFee;

}

<!--포장 옵션 상품선택 변경 관련-->
function handleOnPackageProductSelectChange(selectElement, index) {
  const selectedOption = selectElement.options[selectElement.selectedIndex];
  document.getElementById("orderDetailDtoItemList" + index
      + ".optionProductId").value = selectedOption.dataset.optionProductId;
  document.getElementById("orderDetailDtoItemList" + index
      + ".optionProductName").value = selectedOption.dataset.optionProductName;
  document.getElementById("orderDetailDtoItemList" + index
      + ".optionProductSinglePrice").value = selectedOption.dataset.optionProductPrice;

  updateProductTotalAmountClient();
  updateShippingFee();
  updatePayAmountClient();
  updateExpectedAccumulatingPoint();

  document.getElementById("orderTotalAmount").value = productTotalAmount + shippingFee;
}

<!--캘린더 관련-->
function getMinDeliveryDate() {
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  const year = tomorrow.getFullYear();
  const month = String(tomorrow.getMonth() + 1).padStart(2, '0');
  const day = String(tomorrow.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

function toggleCalendar(checked, calendar) {
  document.getElementById(calendar).hidden = !checked;
}

<!--주소 선택 관련-->
function handleSelectAddress(selectedClientAddressDto) {
  showSelectedAddressInfo();
  updateSelectedAddressInfoData(selectedClientAddressDto);
}

function showSelectedAddressInfo() {
  const selectedAddressInfo = document.getElementById("selectedAddressInfo");
  selectedAddressInfo.children[0].hidden = true;
  selectedAddressInfo.children[1].hidden = false;
}

function updateSelectedAddressInfoData(selectedClientAddressElement) {
  document.getElementById(
      "addressNickname").value = selectedClientAddressElement.children[0].innerText;
  document.getElementById(
      "addressZipCode").value = selectedClientAddressElement.children[1].innerText;
  document.getElementById(
      "deliveryAddress").value = selectedClientAddressElement.children[2].innerText + ", "
      + selectedClientAddressElement.children[3].innerText;
}

function loadAddressList() {

  console.log("주소목록 버튼 선택")

  fetch("/client/order/addressList")
  .then(response => {
    if (!response.ok) {
      throw new Error('API 호출 오류가 발생했습니다.');
    }
    return response.json();
  })
  .then(response => {
    updateAddressList(response);
  })
  .catch(error => {
    console.log(error);
  })

}

function updateAddressList(response) {

  // 모달에 주소 목록 업데이트

  // 주소 선택 모달 바디에 <div class="border border-secondary-subtle rounded" style="margin: 10px; padding: 10px"></div> 추가하기
  const addressSelectModalBody = document.getElementById("address-select-modal-body");

  addressSelectModalBody.innerText = '';

  response.forEach((obj) => {

    const addressContainer = document.createElement('div');
    addressContainer.className = "border border-secondary-subtle rounded";
    addressContainer.style.margin = "10px";
    addressContainer.style.padding = "10px";

    const addressForm = document.createElement('div');

    const addressNickName = document.createElement('div');
    const addressZipCode = document.createElement('div');
    const address = document.createElement('div');
    const addressDetail = document.createElement('div');

    addressNickName.innerText = obj.clientDeliveryAddressNickname;
    addressZipCode.innerText = obj.clientDeliveryZipCode;
    address.innerText = obj.clientDeliveryAddress;
    addressDetail.innerText = obj.clientDeliveryAddressDetail;

    addressForm.append(addressNickName);
    addressForm.append(addressZipCode);
    addressForm.append(address);
    addressForm.append(addressDetail);

    const btn = document.createElement('button');

    btn.type = "button";
    btn.className = "btn btn-outline-secondary";
    btn.dataset.bsDismiss = "modal";

    btn.textContent = "선택";

    btn.dataset.addressNickname = addressNickName.innerText;
    btn.dataset.addressZipcode = addressZipCode.innerText;
    btn.dataset.address = address.innerText;
    btn.dataset.addressDetail = addressDetail.innerText;

    btn.addEventListener('click', function (event) {
      selectAddressBtn(btn);
    });

    addressContainer.append(addressForm);
    addressContainer.append(btn);

    addressSelectModalBody.append(addressContainer);

  })
}

function selectAddressBtn(btn) {
  console.log("주소 선택 버튼: ", btn)
  document.getElementById("addressNickname").textContent = btn.dataset.addressNickname;
  document.getElementById("addressZipCode").value = btn.getAttribute('data-address-zipcode');
  document.getElementById("deliveryAddress").value = btn.getAttribute('data-address') + " , "
      + btn.getAttribute('data-address-detail');
  document.getElementById("selectedAddressInfo").hidden = false;
}

function addressRegisterOnOrderPage(btn) {

  // 유효성 검사
  const zipCode = document.getElementById("clientDeliveryZipCode").value;
  const address = document.getElementById("clientDeliveryAddress").value;
  const detailAddress = document.getElementById("clientDeliveryAddressDetail").value;
  const addressNickname = document.getElementById("clientDeliveryAddressNickname").value;

  if (isEmpty(zipCode)) {
    alert('우편번호를 입력하세요.');
  } else if (isEmpty(address)) {
    alert('주소를 입력하세요.');
  } else if (isEmpty(detailAddress)) {
    alert('상세 주소를 입력하세요.');
  } else if (isEmpty(addressNickname)) {
    alert('주소 별칭을 입력하세요.');
  } else {

    // 버튼 비활성화 및 스피너 활성
    btn.disabled = true;
    const spinner = document.getElementById("addressRegisterBtnSpinner");
    spinner.classList.remove('d-none');

    fetch("/client/order/address", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        "clientDeliveryAddress": address,
        "clientDeliveryAddressDetail": detailAddress,
        "clientDeliveryAddressNickname": addressNickname,
        "clientDeliveryZipCode": parseInt(zipCode)
      })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('번호 등록에 실패했습니다.');
      } else {
        document.getElementById("clientDeliveryAddressNickname").value = null;
        document.getElementById("clientDeliveryZipCode").value = null;
        document.getElementById("clientDeliveryAddress").value = null;
        document.getElementById("clientDeliveryAddressDetail").hidden = true; // 선택 배송지 정보 숨기기
        document.getElementById("addressRegisterModalClose").click(); // 전화번호 등록 모달창 닫기
      }

      // 버튼 활성화 및 스피너 비활성화
      btn.disabled = false;
      spinner.classList.add('d-none');

    })
    .catch(error => {
      alert("주소지 등록에 실패했습니다.\n" + error)
      // 입력필드 초기화
      document.getElementById("postcode").value = null;
      document.getElementById("address").value = null;
      document.getElementById("detailAddress").value = null;
      document.getElementById("addressNickname").value = null;
    })
  }

}

<!--번호 선택 관련-->
function loadPhoneNumberList() {
  fetch("/client/order/phoneNumberList")
  .then(response => {
    if (!response.ok) {
      throw new Error('API 호출 오류가 발생했습니다.');
    }
    return response.json();
  })
  .then(response => {
    updatePhoneNumberList(response);
  })
  .catch(error => {
    console.log("주소 목록을 가져오는데 실패하였습니다.");
    alert("주소 목록을 가져오는데 실패하였습니다. 다시 시도해 주세요!")
  })
}

function updatePhoneNumberList(response) {

  // 모달에 핸드폰 번호 리스트 업데이트

  // 번호 선택 모달 바디에 <div class="border border-secondary-subtle rounded" style="margin: 10px; padding: 10px"></div> 추가하기
  const phoneNumberSelectModalBody = document.getElementById("phoneNumber-select-modal-body");

  phoneNumberSelectModalBody.innerText = '';

  response.forEach((obj) => {

    const phoneNumberContainer = document.createElement('div');
    phoneNumberContainer.className = "border border-secondary-subtle rounded";
    phoneNumberContainer.style.margin = "10px";
    phoneNumberContainer.style.padding = "10px";

    const phoneNumber = document.createElement('div');

    phoneNumber.innerText = obj.clientPhoneNumber;

    phoneNumberContainer.append(phoneNumber);

    const btn = document.createElement('button');

    btn.type = "button";
    btn.className = "btn btn-outline-secondary";
    btn.dataset.bsDismiss = "modal";

    btn.textContent = "선택";

    btn.dataset.phonenumber = phoneNumber.innerText;

    btn.addEventListener('click', function (event) {
      selectPhoneNumberBtn(btn);
    })

    phoneNumberContainer.append(phoneNumber);
    phoneNumberContainer.append(btn);

    phoneNumberSelectModalBody.append(phoneNumberContainer);

  })

}

function selectPhoneNumberBtn(btn) {
  document.getElementById("phoneNumber").value = btn.getAttribute("data-phonenumber");
  document.getElementById("selectedPhoneNumberForm").hidden = false;
}

function phoneNumberRegisterOnOrderPage(btn) {

  const phoneNumber = document.getElementById("newPhoneNumber");

  if (phoneNumber.value.length < 10 || phoneNumber.value.length > 11) {
    phoneNumber.value = "";
    alert("전화번호는 10자리 혹은 11자리여야 합니다.");
  } else {

    // 버튼 비활성화 및 스피너 활성화
    btn.disabled = true;
    const spinner = document.getElementById("phoneNumberRegisterBtnSpinner");
    spinner.classList.remove('d-none');

    fetch("/client/order/phone", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        "phoneNumber": phoneNumber.value
      })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('번호 등록에 실패했습니다.');
      } else {
        phoneNumber.value = "";
        document.getElementById("phoneNumber").value = "";
        document.getElementById("selectedPhoneNumberForm").hidden = true;
        document.getElementById("phoneNumberRegisterModalClose").click(); // 전화번호 등록 모달창 닫기
      }

      // 버튼 활성화 및 스피너 비활성화
      btn.disabled = false;
      spinner.classList.add('d-none');

    })
    .catch(error => {
      alert("번호 등록에 실패했습니다.\n" + error)
      phoneNumber.value = "";
      console.log(error);
    })
  }

}

<!--금액 관련-->
function updateShippingFee() {
  const originShippingFee = parseInt(document.getElementById("originalShippingFee").value);
  const minPurchaseAmount = parseInt(document.getElementById("minPurchasePrice").textContent);

  const productTotalAmount = document.getElementById("productTotalAmount").value;
  if (productTotalAmount >= minPurchaseAmount) {
    document.getElementById("shippingFee").value = 0;
  } else {
    document.getElementById("shippingFee").value = originShippingFee;
  }
  updatePayAmountClient();
  updateExpectedAccumulatingPoint();
}

function updateProductTotalAmountClient() {
  let totalPrice = 0;
  document.querySelectorAll('tr[data-product-index]').forEach(function (row, index) {
    // 순수 상품 총 가격
    const price = parseInt(row.dataset.productPrice);
    const quantity = parseInt(row.dataset.productQuantity);
    const subtotal = price * quantity;
    totalPrice += subtotal;
    // 포장지 총 가격
    const checkBox = row.querySelector('input[type="checkbox"]');
    if (checkBox.checked) {
      const optionProductPrice = parseInt(document.getElementById(
          "orderDetailDtoItemList" + index + ".optionProductSinglePrice").value);
      totalPrice += optionProductPrice;
    }
  });
  document.getElementById("productTotalAmount").value = totalPrice
}

function updatePayAmountClient() {
  const shippingFee = parseInt(document.getElementById("shippingFee").value);
  const productTotalAmount = parseInt(document.getElementById("productTotalAmount").value);
  // 최종 결제 금액
  document.getElementById("payAmount").value = productTotalAmount + shippingFee;
}

function checkValidate(form) {

  console.log("checkValidate");

  let isValid = true;
  let errorMessage = '';

  if (isEmpty(document.getElementById("addressZipCode").value) || isEmpty(
      document.getElementById("deliveryAddress").value)) {
    isValid = false;
    errorMessage += '(필수)주소를 선택해주세요\n';
  }

  if (isEmpty(document.getElementById("phoneNumber").value)) {
    isValid = false;
    errorMessage += '(필수)핸드폰 번호를 선택해 주세요';
  }

  if (isValid) {
    console.log("valid");
    return true;
  } else {
    console.log("invalid");
    alert(errorMessage);
    return false;
  }

}

function isEmpty(value) {
  return value === null || value === "" || value === undefined;
}

function daumPost() {
  new daum.Postcode({
    oncomplete: function (data) {
      var addr = '';
      if (data.userSelectedType === 'R') { // 도로명 주소 선택 시
        addr = data.roadAddress;
      } else { // 지번 주소 선택 시
        addr = data.jibunAddress;
      }
      document.getElementById('clientDeliveryZipCode').value = data.zonecode;
      document.getElementById('clientDeliveryAddress').value = addr;
      document.getElementById('clientDeliveryAddressDetail').focus();
      document.getElementById('postcodeModal').style.display = 'none'; // 모달 닫기
    },
    width: '100%',
    height: '100%'
  }).embed(document.getElementById('postcodeModal'));
  document.getElementById('postcodeModal').style.display = 'flex'; // 모달 열기
}

function closePostcodeModal() {
  document.getElementById('postcodeModal').style.display = 'none'; // 모달 닫기
}

function applyPoints(){
  const originProductTotalAmount = parseInt(document.getElementById("productTotalAmount").value);
  const originShippingFee = parseInt(document.getElementById("originalShippingFee").value);

  let inputUsedPointAmount = parseInt(document.getElementById('inputUsedPointAmount').value); // 사용자가 입력한 사용 포인트
  const remainingPoints = parseInt(document.getElementById('remaining-points').textContent); // 가용포인트
  const couponDiscountAmount = parseInt(document.getElementById("couponDiscountAmount").value); // 쿠폰 할인 금액
  const payAmount = originProductTotalAmount + originShippingFee - couponDiscountAmount // 결제금액 = 포인트 사용 가능 최대치

  console.log("가용 포인트: ", remainingPoints);
  console.log("입력 사용 포인트: ", inputUsedPointAmount);
  console.log("쿠폰 할인 금액: ", couponDiscountAmount);


  if(inputUsedPointAmount < 0){
    document.getElementById('inputUsedPointAmount').value = 0;
    alert("유효하지 않은 포인트 사용값입니다.");
  }
  else if(inputUsedPointAmount > remainingPoints){
    document.getElementById('inputUsedPointAmount').value = 0;
    alert("보유 포인트보다 더 많은 포인트를 사용할 수 없습니다");
  }
  else if(inputUsedPointAmount > payAmount){
    document.getElementById('inputUsedPointAmount').value = 0;
    alert("남은 결제 금액보다 더 많은 포인트를 사용할 수 없습니다");
  }
  else if(1 <= payAmount - inputUsedPointAmount && payAmount - inputUsedPointAmount <= 200){
    document.getElementById('inputUsedPointAmount').value = 0;
    alert("결제 시스템 상 1원 ~ 200원은 결제 불가능합니다.");
  }
  else{
    // 포인트 사용가능!
    document.getElementById("usedPointDiscountAmount").value = inputUsedPointAmount;

    const productTotalPrice = parseInt(document.getElementById("productTotalAmount").value);
    const shippingFee = parseInt(document.getElementById("shippingFee").value);
    const couponDiscountAmount = parseInt(document.getElementById("couponDiscountAmount").value);
    const usedPointDiscountAmount = parseInt(document.getElementById("usedPointDiscountAmount").value);

    // 결제 금액
    document.getElementById("payAmount").value = productTotalPrice + shippingFee - couponDiscountAmount - usedPointDiscountAmount;
    updateExpectedAccumulatingPoint();
  }
}

function updateCouponList(response){

  const couponSelectModalBody = document.getElementById("coupon-select-modal-body");
  couponSelectModalBody.innerHTML = "";

  couponSelectModalBody.className = "border border-secondary-subtle rounded";
  couponSelectModalBody.style.margin = "10px";
  couponSelectModalBody.style.padding = "10px";

  response.forEach((obj) => {

    const couponInfoContainer = document.createElement('div');

    couponInfoContainer.className = "border border-secondary-subtle rounded";
    couponInfoContainer.style.margin = "10px";
    couponInfoContainer.style.padding = "10px";

    // 쿠폰 정책 설명 추가
    const couponPolicyDescriptionDiv = document.createElement('div');
    couponPolicyDescriptionDiv.innerText = obj.couponPolicyDescription;
    couponInfoContainer.append(couponPolicyDescriptionDiv);

    // 할인금액
    const discountValueDiv = document.createElement('div');
    discountValueDiv.innerText = obj.discountValue + obj.discountUnit;
    couponInfoContainer.append(discountValueDiv);

    // 최소 구매 금액
    const minPurchaseAmountDiv = document.createElement('div');
    minPurchaseAmountDiv.innerText = obj.minPurchaseAmount + "원";
    couponInfoContainer.append(minPurchaseAmountDiv);

    // 백분율 쿠폰일 때 최대 할인 금액
    if(obj.discountUnit === '%'){
      const maxDiscountAmount = document.createElement('div');
      maxDiscountAmount.innerText = obj.maxDiscountAmount;
      couponInfoContainer.append(maxDiscountAmount);
    }

    // 사용 선택 버튼
    if(obj.isApplicable){

      const selectBtn = document.createElement('btn');

      selectBtn.type = "button";
      selectBtn.className = "btn btn-outline-secondary";
      selectBtn.dataset.bsDissmiss = "modal";
      selectBtn.textContent = "선택";
      selectBtn.dataset.couponId = obj.couponId;
      selectBtn.dataset.description = obj.couponPolicyDescription;
      selectBtn.dataset.discountValue = obj.discountValue;

      selectBtn.addEventListener('click', function (event){
        selectCoupon(selectBtn);
      })

      couponInfoContainer.append(selectBtn);

    }

    // 사용 불가 버튼
    else{
      const nonApplicableBtn = document.createElement('button');
      nonApplicableBtn.disabled = true;
      nonApplicableBtn.innerText = "사용불가";
      nonApplicableBtn.type = "button";
      nonApplicableBtn.className = "btn btn-outline-danger";
      couponInfoContainer.append(nonApplicableBtn);
      const reason = document.createElement('div');
      reason.innerText = obj.notApplicableDescription;
      reason.style.color = "FF0000FF";
      couponInfoContainer.append(reason);
    }

    couponSelectModalBody.append(couponInfoContainer);

  })
}

function selectCoupon(btn){

  // 포인트 및 쿠폰 초기화
  const productTotalPrice = parseInt(document.getElementById("productTotalAmount").value);
  const shippingFee = parseInt(document.getElementById("shippingFee").value);

  document.getElementById("usedPointDiscountAmount").value = 0;
  document.getElementById('inputUsedPointAmount').value = 0;
  document.getElementById("couponDiscountAmount").value = 0;
  document.getElementById("couponDiscountAmount").value = 0;

  document.getElementById("payAmount").value = productTotalPrice + shippingFee;

  // 선택한 쿠폰 노출
  document.getElementById("couponId").value = parseInt(btn.dataset.couponId);
  document.getElementById("selectedCouponName").textContent = btn.dataset.description;
  document.getElementById("selectedCouponDiscountAmount").textContent = btn.dataset.discountValue;

  document.getElementById("selectedCouponInfo").hidden = false;

  // 선택한 쿠폰 결제 금액에 반영
  document.getElementById("couponDiscountAmount").value = parseInt(btn.dataset.discountValue);
  document.getElementById("payAmount").value = productTotalPrice + shippingFee - parseInt(document.getElementById("couponDiscountAmount").value);

  //  쿠폰모달창 닫기
  document.getElementById("couponSelectModalClose").click();

  updateExpectedAccumulatingPoint()

}

function cancelApplicatedCoupon(){

  const usedPoint = parseInt(document.getElementById("usedPointDiscountAmount").value);

  document.getElementById("couponId").value = null;
  document.getElementById("selectedCouponName").textContent = null;
  document.getElementById("couponDiscountAmount").value = 0;
  document.getElementById("selectedCouponDiscountAmount").textContent = 0;

  const productTotalPrice = parseInt(document.getElementById("productTotalAmount").value);
  const shippingFee = parseInt(document.getElementById("shippingFee").value);
  document.getElementById("payAmount").value = productTotalPrice + shippingFee - usedPoint;

  document.getElementById("selectedCouponInfo").hidden = true;

  updateExpectedAccumulatingPoint()
}
