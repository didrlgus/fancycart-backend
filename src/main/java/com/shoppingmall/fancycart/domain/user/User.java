package com.shoppingmall.fancycart.domain.user;

import com.shoppingmall.fancycart.domain.BaseTimeEntity;

import com.shoppingmall.fancycart.web.dto.UserResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    private String password;

    @Column
    private LocalDate birth;

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

    @Builder
    public User(Long id, String name, String password, String email, LocalDate birth, Role role,
                String roadAddr, String buildingName, String detailAddr, boolean agreeMessageByEmail) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.role = role;
        this.roadAddr = roadAddr;
        this.buildingName = buildingName;
        this.detailAddr = detailAddr;
        this.agreeMessageByEmail = agreeMessageByEmail;
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

    public UserResponseDto.Profile toProfileResponseDto(User user) {
        return UserResponseDto.Profile.builder()
                .name(user.getName())
                .email(user.getEmail())
                .birth(user.getBirth())
                .agreeMessageByEmail(user.isAgreeMessageByEmail() ? "YES" : "NO")
                .build();
    }
}
