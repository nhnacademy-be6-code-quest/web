//package com.nhnacademy.codequestweb.controllerAdvice;
//
//import feign.FeignException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//@Slf4j
//public class FeignExceptionControllerAdvice {
//
//    @ExceptionHandler(FeignException.class)
//    public ResponseEntity<ErrorResponseDto> handleFeignException(FeignException e) {
//        String errorMessage = extractErrorMessage(e.getMessage());
//        log.warn("status : {}, message : {}",e.status(), errorMessage);
//        return ResponseEntity.status(e.status()).body(new ErrorResponseDto(errorMessage));
//    }
//
//    private String extractErrorMessage(String feignMessage) {
//        try {
//            // JSON 메시지의 실제 오류 메시지 부분을 추출하는 로직
//            int startIndex = feignMessage.indexOf("{\"message\":\"") + "{\"message\":\"".length();
//            int endIndex = feignMessage.indexOf("\"}", startIndex);
//            if (startIndex > 0 && endIndex > startIndex) {
//                return feignMessage.substring(startIndex, endIndex);
//            }
//        } catch (Exception ex) {
//            log.error("Error parsing FeignException message", ex);
//        }
//        // 기본 오류 메시지 반환
//        return "Not expected error occurred";
//    }
//
//}
