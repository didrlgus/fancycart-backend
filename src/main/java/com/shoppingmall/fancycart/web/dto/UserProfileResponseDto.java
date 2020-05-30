package com.shoppingmall.fancycart.web.dto;

import com.shoppingmall.fancycart.domain.user.User;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private String roadAddr;
    private String buildingName;
    private String detailAddr;
    private boolean agreeMessageByEmail;

    public UserProfileResponseDto(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.picture = entity.getPicture();
        this.roadAddr = entity.getRoadAddr();
        this.buildingName = entity.getBuildingName();
        this.detailAddr = entity.getDetailAddr();
        this.agreeMessageByEmail = entity.isAgreeMessageByEmail();
    }
}
