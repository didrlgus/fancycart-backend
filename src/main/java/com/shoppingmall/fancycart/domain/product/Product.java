package com.shoppingmall.fancycart.domain.product;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;
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
    private String category;

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

}
