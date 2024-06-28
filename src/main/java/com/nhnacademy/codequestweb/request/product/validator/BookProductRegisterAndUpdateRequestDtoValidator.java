//package com.nhnacademy.codequestweb.request.product.validator;
//
//import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
//import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.TypeMismatchException;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
//
//@Slf4j
//@Component
//public class BookProductRegisterAndUpdateRequestDtoValidator implements Validator {
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return clazz.equals(BookProductRegisterRequestDto.class) || clazz.equals(BookProductUpdateRequestDto.class);
//    }
//
//    @Override
//    public void validate(Object target, Errors errors) {
//        log.error("Validating BookProductRegisterRequestDto");
//        List<String> categoryNameList;
//        if (target instanceof BookProductRegisterRequestDto) {
//            BookProductRegisterRequestDto bookProductRegisterRequestDto = (BookProductRegisterRequestDto) target;
//            categoryNameList = bookProductRegisterRequestDto.categories();
//        } else if (target instanceof  BookProductUpdateRequestDto) {
//            BookProductUpdateRequestDto bookProductUpdateRequestDto = (BookProductUpdateRequestDto) target;
//            categoryNameList = bookProductUpdateRequestDto.categories();
//        } else {
//            throw new TypeMismatchException(target.getClass(), BookProductRegisterRequestDto.class);
//        }
//        if (categoryNameList == null || categoryNameList.isEmpty()) {
//            errors.rejectValue("categories", "must.have.category", "must have at least one category");
//        }
//    }
//}
