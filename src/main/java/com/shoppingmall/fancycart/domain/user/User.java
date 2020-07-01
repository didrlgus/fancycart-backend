package com.shoppingmall.fancycart.domain.user;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String roadAddr;

    @Column
    private String buildingName;

    @Column
    private String detailAddr;

    @Column
    private boolean agreeMessageByEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String token;

    @Builder
    public User(Long id, String name, String email, Role role,
                String roadAddr, String buildingName, String detailAddr, boolean agreeMessageByEmail, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.roadAddr = roadAddr;
        this.buildingName = buildingName;
        this.detailAddr = detailAddr;
        this.agreeMessageByEmail = agreeMessageByEmail;
        this.token = token;
    }

    public User update(String name) {
        this.name = name;

        return this;
    }

    public User update(String name, String roadAddr, String buildingName,
                       String detailAddr, boolean agreeMessageByEmail) {
        this.name = name;
        this.roadAddr = roadAddr;
        this.buildingName = buildingName;
        this. detailAddr = detailAddr;
        this.agreeMessageByEmail = agreeMessageByEmail;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
