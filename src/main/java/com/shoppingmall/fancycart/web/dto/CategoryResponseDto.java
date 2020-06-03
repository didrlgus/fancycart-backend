package com.shoppingmall.fancycart.web.dto;

import com.shoppingmall.fancycart.domain.category.Category;
import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private Long id;
    private String catCd;
    private String catNm;
    private String upprCatCd;
    private Integer catLv;
    private Character isAvailable;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.catCd = category.getCatCd();
        this.catNm = category.getCatNm();
        this.upprCatCd = category.getUpprCatCd();
        this.catLv = category.getCatLv();
        this.isAvailable = category.getIsAvailable();
    }
}
