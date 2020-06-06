package com.shoppingmall.fancycart.domain.user;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;

import com.shoppingmall.fancycart.domain.tag.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private String picture;

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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags = new HashSet<>();

    @Builder
    public User(Long id, String name, String email, String picture, Role role,
                String roadAddr, String buildingName, String detailAddr, boolean agreeMessageByEmail) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.roadAddr = roadAddr;
        this.buildingName = buildingName;
        this. detailAddr = detailAddr;
        this.agreeMessageByEmail = agreeMessageByEmail;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public User update(String roadAddr, String buildingName,
                       String detailAddr, boolean agreeMessageByEmail) {
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
