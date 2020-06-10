package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class CategoryRequestDto {
    @NotNull
    @Length(max = 50)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{1,20}$")
    private String catNm;
    @Length(min = 7, max = 7)
    private String upprCatCd;
    @Max(3)
    @NotNull
    private Integer catLv;
    @NotNull
    private Character isAvailable;

    @Builder
    public CategoryRequestDto(String catNm, String upprCatCd,
                              Integer catLv, Character isAvailable) {
        this.catNm = catNm;
        this.upprCatCd = upprCatCd;
        this.catLv = catLv;
        this.isAvailable = isAvailable;
    }
}
