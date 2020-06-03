package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class CategoryUpdateRequestDto {
    @NotNull
    @Length(max = 50)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{1,20}$")
    private String catNm;

    @NotNull
    private Character isAvailable;

    @Builder
    public CategoryUpdateRequestDto(String catNm, Character isAvailable) {
        this.catNm = catNm;
        this.isAvailable = isAvailable;
    }
}
