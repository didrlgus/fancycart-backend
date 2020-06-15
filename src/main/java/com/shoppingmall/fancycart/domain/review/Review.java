package com.shoppingmall.fancycart.domain.review;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;
import com.shoppingmall.fancycart.domain.product.Product;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.web.dto.ReviewRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Lob
    private String content;

    @Column
    private int rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")       // 연관관계의 주인 -> 외래키 가짐
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")    // 연관관계의 주인
    private Product product;

    @Builder
    public Review(User user, Product product, ReviewRequestDto reviewRequestDto) {
        this.user = user;
        this.product = product;
        this.title = reviewRequestDto.getTitle();
        this.content = reviewRequestDto.getContent();
        this.rate = reviewRequestDto.getRate();
    }
}
