package com.shoppingmall.fancycart.domain.product;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;
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
    private Integer limitCount;

    @Column
    private Integer totalCount;

    @Column(length = 100)
    private String titleImg;

    @Column
    @ColumnDefault("0") //default 0
    private Integer rateAvg;

    @Builder
    public Product(String productNm, String largeCatCd, String smallCatCd,
                   Integer price, Integer purchaseCount, Integer totalCount,
                   Integer limitCount, String titleImg, Integer rateAvg) {
        this.productNm = productNm;
        this.largeCatCd = largeCatCd;
        this.smallCatCd = smallCatCd;
        this.price = price;
        this.purchaseCount = purchaseCount;
        this.totalCount = totalCount;
        this.limitCount = limitCount;
        this.titleImg = titleImg;
        this.rateAvg = rateAvg;
    }
}
