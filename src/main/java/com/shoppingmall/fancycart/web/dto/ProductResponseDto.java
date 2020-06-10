package com.shoppingmall.fancycart.web.dto;

import com.shoppingmall.fancycart.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductResponseDto {
    private Long id;

    private String productNm;

    private Integer price;

    private String titleImg;

    private Integer rateAvg;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.productNm = product.getProductNm();
        this.price = product.getPrice();
        this.titleImg = product.getTitleImg();
        this.rateAvg = product.getRateAvg();
    }
}
