package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class ProductRequestDto {
    @Getter
    public static class Get {
        @Min(1)
        private Integer page;
        @NotBlank
        private String category;

        @Builder
        public Get(Integer page, String category) {
            this.page = page;
            this.category = category;
        }
    }
}
