package com.nhnacademy.codequestweb.request.product.validator;

import com.nhnacademy.codequestweb.request.product.productCategory.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.productCategory.CategoryUpdateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class ProductCategoryRegisterAndUpdateRequestDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(CategoryRegisterRequestDto.class) || clazz.equals(CategoryUpdateRequestDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("Validating CategoryRegisterRequestDto");
        String categoryName;
        if (target instanceof CategoryRegisterRequestDto categoryRegisterRequestDto) {
            categoryName = categoryRegisterRequestDto.categoryName();
        } else if (target instanceof CategoryUpdateRequestDto categoryUpdateRequestDto) {
            categoryName = categoryUpdateRequestDto.newCategoryName();
        } else {
            categoryName = "";
        }
        if (categoryName.contains(",")) {
            errors.rejectValue(target instanceof CategoryRegisterRequestDto? "categoryName" : "newCategoryName", "categoryName.invalid", "Invalid category name");
        }
    }
}
