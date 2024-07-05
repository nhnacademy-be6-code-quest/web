package com.nhnacademy.codequestweb.config;

import com.nhnacademy.codequestweb.client.product.category.CategoryClient;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryNodeResponseDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class CategoryConfig {
    private final CategoryClient categoryClient;
    @Getter
    private CategoryNodeResponseDto root;

    public CategoryConfig(@Lazy CategoryClient categoryClient) {
        this.categoryClient = categoryClient;
        init();
    }

    public void init() {
        try {
            this.root = categoryClient.getCategoriesTree().getBody();
            log.info("root: {}", this.root);
        } catch (Exception e) {
            log.error("Error while getting category tree", e);
        }
    }

    public void update(CategoryNodeResponseDto root) {
        this.root = root;
    }
}
