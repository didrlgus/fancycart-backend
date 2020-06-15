package com.shoppingmall.fancycart.web.dto;

import com.shoppingmall.fancycart.domain.product.Product;
import com.shoppingmall.fancycart.domain.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductResponseDto {
    private Long id;

    private String productNm;

    private Integer price;

    private String titleImg;

    private Integer rateAvg;

    @Builder
    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.productNm = product.getProductNm();
        this.price = product.getPrice();
        this.titleImg = product.getTitleImg();
        this.rateAvg = product.getRateAvg();
    }

    @Getter
    @Builder
    public static class Details {
        private Long id;

        private String productNm;

        private String largeCatCd;

        private String smallCatCd;

        private Integer price;

        private Integer purchaseCount;

        private Integer totalCount;

        private String titleImg;

        private Integer rateAvg;

        private String fullDescription;

        private List<ReviewResponseDto> reviewResponseDtoList;
    }
}
