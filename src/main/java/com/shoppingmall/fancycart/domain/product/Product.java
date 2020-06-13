package com.shoppingmall.fancycart.domain.product;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;
import com.shoppingmall.fancycart.web.dto.ProductRequestDto;
import com.shoppingmall.fancycart.web.dto.ProductResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String productNm;

    @Column(length = 10)
    private String largeCatCd;

    @Column(length = 10)
    private String smallCatCd;

    @Column
    private Integer price;

    @Column
    @ColumnDefault("0")
    private Integer purchaseCount;

    @Column
    private Integer totalCount;

    @Column(length = 100)
    private String titleImg;

    @Column
    @ColumnDefault("0") //default 0
    private Integer rateAvg;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Builder
    public Product(String productNm, String largeCatCd, String smallCatCd,
                   Integer price, Integer purchaseCount, Integer totalCount,
                   String titleImg, Integer rateAvg, String fullDescription) {
        this.productNm = productNm;
        this.largeCatCd = largeCatCd;
        this.smallCatCd = smallCatCd;
        this.price = price;
        this.purchaseCount = purchaseCount;
        this.totalCount = totalCount;
        this.titleImg = titleImg;
        this.rateAvg = rateAvg;
        this.fullDescription = fullDescription;
    }

    public static Product toProduct(ProductRequestDto productRequestDto) {
        return Product.builder()
                .productNm(productRequestDto.getProductNm())
                .price(productRequestDto.getPrice())
                .largeCatCd(productRequestDto.getLargeCatCd())
                .smallCatCd(productRequestDto.getSmallCatCd())
                .fullDescription(productRequestDto.getFullDescription())
                .totalCount(productRequestDto.getTotalCount())
                .titleImg(productRequestDto.getTitleImg())
                .build();
    }

    public Product update(ProductRequestDto productRequestDto) {
        this.productNm = productRequestDto.getProductNm();
        this.price = productRequestDto.getPrice();
        this.largeCatCd = productRequestDto.getLargeCatCd();
        this.smallCatCd = productRequestDto.getSmallCatCd();
        this.totalCount = productRequestDto.getTotalCount();
        this.titleImg = productRequestDto.getTitleImg();
        this.fullDescription = productRequestDto.getFullDescription();

        return this;
    }

    public ProductResponseDto.Details toProductDetailsResponseDto(Product product) {
        return ProductResponseDto.Details.builder()
                .id(product.getId())
                .productNm(product.getProductNm())
                .price(product.getPrice())
                .totalCount(product.getTotalCount())
                .rateAvg(product.getRateAvg())
                .purchaseCount(product.getPurchaseCount())
                .fullDescription(product.getFullDescription())
                .titleImg(product.getTitleImg())
                .smallCatCd(product.getSmallCatCd())
                .largeCatCd(product.getLargeCatCd())
                .build();
    }
}
