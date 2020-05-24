package com.shoppingmall.fancycart.web.dto;

import com.shoppingmall.fancycart.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileRequestDto {
    private String roadAddr;
    private String buildingName;
    private String detailAddr;

    @Builder
    public UserProfileRequestDto(String roadAddr, String buildingName, String detailAddr) {
        this.roadAddr = roadAddr;
        this.buildingName = buildingName;
        this.detailAddr = detailAddr;
    }

    public User toEntity() {
        return User.builder()
                .roadAddr(roadAddr)
                .buildingName(buildingName)
                .detailAddr(detailAddr)
                .build();
    }
}
