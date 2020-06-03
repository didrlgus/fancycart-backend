package com.shoppingmall.fancycart.domain.category;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;
import com.shoppingmall.fancycart.domain.tag.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String catCd;

    @Column(length = 50)
    private String catNm;

    @Column(length = 10)
    private String upprCatCd;

    @Column(length = 11)
    private Integer catLv;

    @Column
    private Character isAvailable;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public Category(Long id, String catCd, String catNm, String upprCatCd,
                    Integer catLv, Character isAvailable, Tag tag) {
        this.id = id;
        this.catCd = catCd;
        this.catNm = catNm;
        this.upprCatCd = upprCatCd;
        this.catLv = catLv;
        this.isAvailable = isAvailable;
        this.tag = tag;
    }

    public Category update(String catNm, Character isAvailable) {
        this.catNm = catNm;
        this.isAvailable = isAvailable;
        this.tag = tag.update(catNm);

        return this;
    }
}