package com.shoppingmall.fancycart.domain.user;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Column
    private String roadAddr;

    @Column
    private String buildingName;

    @Column
    private String detailAddr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role,
                String roadAddr, String buildingName, String detailAddr) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.roadAddr = roadAddr;
        this.buildingName = buildingName;
        this. detailAddr = detailAddr;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public User update(String roadAddr, String buildingName, String detailAddr) {
        this.roadAddr = roadAddr;
        this.buildingName = buildingName;
        this. detailAddr = detailAddr;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
