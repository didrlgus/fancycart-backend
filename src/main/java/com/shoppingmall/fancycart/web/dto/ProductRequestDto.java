package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class ProductRequestDto {

    @NotBlank
    @Length(max = 100)
    @NotNull
    private String productNm;

    @Length(min = 7, max = 7)
    @NotNull
    private String largeCatCd;

    @Length(min = 7, max = 7)
    @NotNull
    private String smallCatCd;

    @Min(0)
    @NotNull
    private Integer price;

    @Min(1)
    @NotNull
    private Integer totalCount;

    @NotBlank
    @NotNull
    private String titleImg;

    @NotBlank
    @NotNull
    private String fullDescription;

    @Getter
    public static class Get {
        @NotNull
        @Min(1)
        private Integer page;
        @Length(min = 7, max = 7)
        private String categoryCd;

        @Builder
        public Get(Integer page, String categoryCd) {
            this.page = page;
            this.categoryCd = categoryCd;
        }
    }
}
