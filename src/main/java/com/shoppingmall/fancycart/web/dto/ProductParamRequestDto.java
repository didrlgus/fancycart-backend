package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class ProductParamRequestDto {
    @NotNull
    @Min(1)
    private Integer page;
    @Length(min = 7, max = 7)
    private String categoryCd;

    @Builder
    public ProductParamRequestDto(Integer page, String categoryCd) {
        this.page = page;
        this.categoryCd = categoryCd;
    }
}
