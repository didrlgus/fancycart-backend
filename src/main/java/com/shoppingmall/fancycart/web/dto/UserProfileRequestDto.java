package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class UserProfileRequestDto {
    @NotBlank
    @Length(max = 50)
    private String roadAddr;
    @NotBlank
    @Length(max = 50)
    private String buildingName;
    @NotBlank
    @Length(max = 50)
    private String detailAddr;
    private boolean agreeMessageByEmail;

    @Builder
    public UserProfileRequestDto(String roadAddr, String buildingName,
                                 String detailAddr, boolean agreeMessageByEmail) {
        this.roadAddr = roadAddr;
        this.buildingName = buildingName;
        this.detailAddr = detailAddr;
        this.agreeMessageByEmail = agreeMessageByEmail;
    }
}
