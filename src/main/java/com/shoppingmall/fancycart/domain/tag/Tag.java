package com.shoppingmall.fancycart.domain.tag;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Tag extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Builder
    public Tag(String title) {
        this.title=title;
    }

    public Tag update(String title) {
        this.title = title;
        return this;
    }
}
