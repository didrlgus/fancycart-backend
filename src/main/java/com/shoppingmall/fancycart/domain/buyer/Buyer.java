package com.shoppingmall.fancycart.domain.buyer;

import com.shoppingmall.fancycart.domain.product.Product;
import com.shoppingmall.fancycart.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")    // 연관관계의 주인
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")    // 연관관계의 주인
    private Product product;

    @Builder
    public Buyer(User user, Product product) {
        this.user = user;
        this.product = product;
    }
}
